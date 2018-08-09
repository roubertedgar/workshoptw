# Defining layers of our project with MVVM

We was making a lot of things on the view, making our activities break the single responsibility principle. So, to avoid this principle breaks, we decide to use  an architecture pattern.

Today we have 3 well know architectures that you should read more about and see what is best for your project:
-  [MVC](https://medium.com/upday-devs/android-architecture-patterns-part-1-model-view-controller-3baecef5f2b6)	Model View Controller
-	[MVP](https://medium.com/upday-devs/android-architecture-patterns-part-2-model-view-presenter-8a6faaae14a5) Model View Presenter - Can have other variants, like [VIPER](http://luboganev.github.io/blog/clean-architecture-pt1/)
-	[MVVM](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b) Model View ViewModel 

In this Step we gonna use MVVM, because we are using ReactiveX to make our asynchronous works, like fetch places from the database. Once MVVM is data stream based, it will make our job easier.

## The Movel-View-ViewModel architecture
-   The  View— that informs the ViewModel about the user’s actions
-   The  ViewModel — exposes streams of data relevant to the View
-   The  DataModel — abstracts the data source. The ViewModel works with the DataModel to get and save the data.

While on MVP architecture we have a two way interface that makes the View knows the presenter and the Presenter knows the view, the ViewModel (Like the presenter) of MVVM just expose streams to the view, so, the ViewModel don't knows the View, avoiding to much knowledge between the layers. You can se a MVVM schema below:

![enter image description here](https://cdn-images-1.medium.com/max/800/0*5mD214cjNXU-V6lf.png)



## Refactoring

Ok, now we know the basics of MVVM, we can start refactoring our code.

Lets starts creating a ViewModel for our [PlaceFormActivity](https://github.com/roubertedgar/workshoptw/blob/step-4/app/src/main/java/workshoptw/com/work_shop_tw/views/place/PLaceFormActivity.kt)

```kotlin
	class  PlaceViewModel() 
``` 
and adding the ViewModel at the PlaceFormActivity

```kotlin
private lateinit var placeViewModel: PlaceViewModel
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_place_form)
	placeViewModel=PlaceViewModel(FactoryDAO.getPlaceDatabase(applicationContext))
	...
	...
``` 

Nice =D, now our view have a ViewModel. So, if we look at the PlaceFormActivity, we will see that PlaceFormActivity returns a place to the MainActivity, and then, the MainActivity saves it.
This is not good, we should make the PlaceFormActivity tells the ViewModel to save the Place for us, right? So, let's continue our refactoring, and see what we can do...

The Place that we want to save belongs to the Model layer, so, our ViewModel should work with our Model layer, right? 
The Idea here is make the View pass the created Place to the ViewModel and tells it to save the created Place. After that, our ViewModel should calls the Model layer to save the Place for us. 

First things first, let's tells the ViewModel to save our place passing the created Place:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	...
	...
	placeViewModel = PlaceViewModel()
		doneButton.setOnClickListener {
			val name = placeName.text.toString()
			val description = placeDescription.text.toString()
			val place = Place(name, description)

			placeViewModel.savePlace(place)
		}
}
```

Now, we gonna implements the save method on the PlaceViewModel:

```kotlin
class PlaceViewModel() {
	fun savePlace(place: Place)
```
To save our place we have to use our PlaceDAO::save(place:Place). For that, lets receive a PlaceDAO at the constructor of the PlaceViewModel:
```kotlin
class PlaceViewModel(private  val  database: PlaceDAO) {
	fun savePlace(place: Place){
		database.save(place)
	}
```

Continuing... following the MVVM  specifications, the ViewModel should expose streams to the view, but until now we are't doing this. The idea is return a stream to the view, once we are working with ReactiveX, we could return a Observable to the view on save a Place. 

If you look at the save method of PlaceDAO, you will see that are no returns. For that, the best choice is use the Completable observer of ReactiveX, that completes an action and tells if it was successful or not.

```kotlin
class PlaceViewModel(private  val  database: PlaceDAO) {
	fun savePlace(place: Place):Completable{
		Completable.fromAction { database.insert(place) }
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
	}
```
The ```subscribeOn(Schedulers.io())``` and the ```observeOn(AndroidSchedulers.mainThread())``` after call the Completable.fromAction is just to execute this action on the ReactiveX IO thread and notify the result on the Android Main Thread. We have to use this because the Android system don't let us doing database access or some other hard work at the Main Thread.

Ok, i think we done with the PlaceViewModel for now. Let's back to the  PlaceFormActivity. 

The PlaceViewModel instantiation are asking to the PlaceDAO, to solve it, we gonna use the FactoryDAO to instantiate the PlaceDAO for us and gives it to the PlaceViewModel constructor inner PlaceFormActivity.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	placeViewModel = PlaceViewModel(FactoryDAO.getPlaceDatabase(applicationContext))
}
```
Once the getPlaceDatabase needs a Context to create our PlaceDAO instance, we just need to pass it by.









<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE3MjMwNjYzMDQsODAwMzE1NDEwLC0xNj
k1NTU4MDQyLDE3MTYwOTk0NzgsMTQ3MjU4Nzk0NSwtMjEzNDIw
MzMyNSwxOTU0MTM1NTg3LDEyMjI4MjczMTksMTYxOTM2Nzg0LC
0xOTk5NDU4MTg2LC05NzI5NDc5NywtMTIzMDA0MTc2OCwyODA3
ODg4MzldfQ==
-->
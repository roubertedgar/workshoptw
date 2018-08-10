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

### PlaceFormActivity 
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

First things first, let's remove the the result activity logic from PlaceFormActivity
```kotlin
//Remove the code below from setOnClickListener{} block
intent.putExtra("place", place)
setResult(Activity.RESULT_OK, intent)
finish()
```
 and tells the ViewModel to save our place passing the created Place witch will be:

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
To save our place we have to use our [PlaceDAO](https://github.com/roubertedgar/workshoptw/blob/step-4/app/src/main/java/workshoptw/com/work_shop_tw/models/place/PlaceDAO.kt)::save(place:Place). For that, lets receive a [PlaceDAO](https://github.com/roubertedgar/workshoptw/blob/step-4/app/src/main/java/workshoptw/com/work_shop_tw/models/place/PlaceDAO.kt) at the constructor of the PlaceViewModel:
```kotlin
class PlaceViewModel(private  val  database: PlaceDAO) {
	fun savePlace(place: Place){
		database.save(place)
	}
```

Continuing... following the MVVM  specifications, the ViewModel should expose streams to the view, but until now we are't doing this. The idea is return a stream to the view, once we are working with ReactiveX, we could return a Observable to the view on save a Place. 

If you look at the save method of [PlaceDAO](https://github.com/roubertedgar/workshoptw/blob/step-4/app/src/main/java/workshoptw/com/work_shop_tw/models/place/PlaceDAO.kt), you will see that are no returns. For that, the best choice is use the Completable observer of ReactiveX, that completes an action and tells if it was successful or not.

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

**Continuing...** On the PlaceFormActivity we are calling the PlaceViewModel::save(Place) when we do a click at the Done button. Ok, the save method of PlaceViewModel returns a Observable, so, we have to subscribe at it and when the action is completed, we should finalize the PlaceFormActivity.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	...
	...
	placeViewModel = PlaceViewModel()
		doneButton.setOnClickListener {
			val name = placeName.text.toString()
			val description = placeDescription.text.toString()
			val place = Place(name, description)

			placeViewModel.savePlace(place).subscribe{finish()}
		}
}
```
done, now, when we save a place and this action is completed, we go back to the MainActivity, once PlaceFormActivity was finished. Here is the final version of PlaceFormActivity with a little refactoring, just pass the save action to a method.

```kotlin 
class PLaceFormActivity : AppCompatActivity() {
	private lateinit var placeViewModel: PlaceViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_place_form)
		placeViewModel = PlaceViewModel(FactoryDAO.getPlaceDatabase(applicationContext))
		doneButton.setOnClickListener {
			val name = placeName.text.toString()
			val description = placeDescription.text.toString()
			val place = Place(name, description)
			savePlace(place)
		}
	}
	private fun savePlace(place: Place) = placeViewModel.savePlace(place).subscribe { finish() }
}
```

### MainActivity

  **Removing on activity result logic**

On the MainActivity we can remove the onActivityResult logic, once PlaceFormActivity has no more results returned.

```kotlin
//Remove this block of code
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	if (resultCode == Activity.RESULT_OK) {
		if (requestCode == PLACE_FORM_REQUEST_CODE) {
			val place = data?.getSerializableExtra("place") as Place
			insertPlace(place)
		}
	}
}
``` 
Previously we was calling the PlaceFormActivity using the ```startActivityForResult```. We can switch it by 
```startActivity()``` for the same reason that we just remove the onActivityResult logic

```kotlin
addButton.setOnClickListener {
	val intent = Intent(this, PlaceFormActivity::class.java)
	startActivity(intent)
}
```
**Using ViewModel on MainActivity**
If you look at the MainActivity, y'll see that we are fetching our places directly on the View. Following the MVVM rules, we gonna use a ViewModel to work with Model layer for us. One thing here is that we already have a ViewModel for Place, and we can reuse it adding the fetch method that will returns all place to the MainActivity.

**Ok, starting by the PlaceViewModel...** we need a method that will return all places for us, so, we start to moving the fetch logic from MainActivity to PlaceViewModel:

Is just copy the loadPlaces method and paste on the PlaceViewModel:


```kotlin 
private fun loadPlaces() {
placeDAO.getAll()
.subscribeOn(Schedulers.io())
.observeOn(AndroidSchedulers.mainThread())
.subscribe {
placeList.clear()
placeList.addAll(it)
recyclerPlaceList.adapter.notifyDataSetChanged()
}
}
```


<!--stackedit_data:
eyJoaXN0b3J5IjpbLTY3MzM0OTc0MywtMzc5MTAzMjc3LC03NT
M1MzE5NTQsMTAwMjM4NTE3MywxMzM1MTI1MjM0LC0xNjM5NTM2
NjA0LDExNTcxMTE2MjgsNDAzODczODEzLC00MjI5MTgzMTYsOD
AwMzE1NDEwLC0xNjk1NTU4MDQyLDE3MTYwOTk0NzgsMTQ3MjU4
Nzk0NSwtMjEzNDIwMzMyNSwxOTU0MTM1NTg3LDEyMjI4MjczMT
ksMTYxOTM2Nzg0LC0xOTk5NDU4MTg2LC05NzI5NDc5NywtMTIz
MDA0MTc2OF19
-->
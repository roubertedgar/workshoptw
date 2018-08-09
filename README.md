Unit tests =D

Ok, now we have an application that creates, store and fetch places from database with a well defined layers by the MVVM architecture. Right, but we want to make some changes on our system. Put new features and things like that.

So, thinking about scalability, we had to provide a way to ensure that changes on our app don't became a problem. Making programmers feel comfortable to work with our code. For that, we have unit tests.

In the [step 4](https://github.com/roubertedgar/workshoptw/tree/step-4)  we decide to use MVVM as the architecture for our application. Once that we want to have well defined layers that make our app easy to test. All right, is test Tiiimeee!!!

Ok,  lets start unitarily testing [PlaceViewModel](https://github.com/roubertedgar/workshoptw/blob/step-5/app/src/main/java/workshoptw/com/work_shop_tw/views/place/PlaceViewModel.kt) class using [Mockito Kotlin](https://github.com/nhaarman/mockito-kotlin), JUnit and some features of ReactiveX that helps us test easy.

So, in [PlaceViewModel](https://github.com/roubertedgar/workshoptw/blob/step-5/app/src/main/java/workshoptw/com/work_shop_tw/views/place/PlaceViewModel.kt) we have two interfaces of communication. One is that saves our place and other is the interface that loads all places that we have.

**Starting with the save method**: The save method receives a place as parameter and returns nothing to us. For that reason we use Completable Observer of ReactiveX. Completable emits no item, just complete a task. So, when we call **PlaceViewModel::save(place)** we receive a Completable that we can subscribe.

   ```kotlin
    fun savePlace(place: Place): Completable{
	    return Completable.fromAction {database.insert(place) } 
     		    .subscribeOn(Schedulers.io()) 
     		    .observeOn(AndroidSchedulers.mainThread())
	}
   ```

To test the function above we have to mock the [PlaceDAO](https://github.com/roubertedgar/workshoptw/blob/step-5/app/src/main/java/workshoptw/com/work_shop_tw/models/place/PlaceDAO.kt), create a PlaceViewModel variable passing the mocked PlaceDAO into PlaceViewModel constructor, like:

```kotlin
	@Mock lateinit var dao: PlaceDAO 
	private lateinit var viewModel: PlaceViewModel
	 
	@Before fun setUp() {
		viewModel = PlaceViewModel(dao) 
	}
```

After that we can finaly create our test...

```kotlin
	@Test fun completeWhenSavePlace() {
		viewModel.savePlace(Place("name", "description")) 
		.test() 
		.assertNoValues() 
		.assertComplete() 
	}
```

So, here we call the viewModel.savePlace that returns to us a Completable Observer from ReactiveX. In ReactiveX Observables we can call test( ), that give us a TestSubscriber that allows us to make some assertions. In the case is noValues, once Completable emits no item, and, assertComplete to make
sure that save action was successful. 

Ok, if we run our test now we will have a red result. Thats because we subscribe on a Thread and Observe on AndroidMainThread in the view model
```kotlin
.subscribeOn(Schedulers.io())
.observeOn(AndroidSchedulers.mainThread()
```

 it means that  we are doing this stuff in background. So we need to use the ReactiveX plugins to make sure that all tasks execute immediately.

Here we can crate a Helper that setups the Reactive plugins, making all Schedulers immediate.
 ```kotlin 
 object RxSchedulerTestSetup {
	fun setupRxScheduler() {
	    val immediate = object : Scheduler() {
			    override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
			        return super.scheduleDirect(run, 0, unit)
			    }	
			    override fun createWorker(): Scheduler.Worker {
			        return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
			    }
		   }
   
	    RxJavaPlugins.setInitIoSchedulerHandler { immediate }
	    RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
	    RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
	    RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
	    RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate 	}
	}

	fun reset() {
	    RxAndroidPlugins.reset()
	    RxJavaPlugins.reset()
	}
}
```
After that we need to setup the schedulers calling setupRxScheduler on @Before method in PlaceViewModelTest:

```kotlin
@Before fun setUp() {
	RxSchedulerTestSetup.setupRxScheduler()
	viewModel = PlaceViewModel(dao) 
 }

//To make sure that all became normal after test we make a reset
@After, like this: @After fun tearDown() {
	RxSchedulerTestSetup.reset() 
}
```

**Now we can run our first test :)**

It's time to write a test for getAll() method in PlaceViewModel. The method getAll() returns 0...N Places to us. In this case we should test the empty return scenery, and the 1 or N return scenery, right?
 But first... let's look at the getAll() in [PlaceDAO](https://github.com/roubertedgar/workshoptw/blob/step-5/app/src/main/java/workshoptw/com/work_shop_tw/models/place/PlaceDAO.kt)

```kotlin
@Query("Select * from Place")
fun getAll(): Flowable<List<Place>>
```
PlaceDAO::getAll() returns a Flowable, that is a observable of ReactiveX, it's like a simple Observable<T> plus BackPressure Strategy, but we can avoid this BackPressure thing for now.

Ok, now we knew what getAll() returns, let's write our second test for our first scenery, where's no place are returned. It will be like our first tests, but now we can test the values returned by the observable.

Calling viewModel.getAll() returns to us a ReactiveX observable, so we can make ".test()" and assert some stuffs, like:

```kotlin
viewModel.getAll()
	.test()
    .assertNoValues()
    .assertComplete()
````
    
If we run this test it will give us a null pointer exception. That's because we don't stub the placeDAO.getAll().
So, placeDAO.getAll() should return a Flowable that respond with a empty value:
```kotlin
 whenever(dao.getAll()).thenReturn(Flowable.empty())
 ```
The final version of our test will be like:

```kotlin
@Test
fun emptyWhenAreNoItemsSaved() {
    whenever(dao.getAll()).thenReturn(Flowable.empty())

    viewModel.getAll().test()
            .assertNoValues()
            .assertComplete()
}
```

Ok, we test the empty case. Lets test when we have some item...

Following the same path of code above, we have to stubs the responde of placeDAO.getAll(). But now, placeDAO.getAll() should return a observable that emits a item at least.

```kotlin
whenever(dao.getAll()).thenReturn(Flowable.just(getPlaces()))
```

The get places method is a helper method to us:

```kotlin
private fun getPlaces(): List<Place> =
 listOf(Place("", ""), Place("", ""))
```
Now we can assert that our returned observable contains values:
```kotlin
 viewModel.getAll().test()
            .assertValue {it.size == 2}
            .assertComplete() 
```
In the code above we just assert that our result has 2 places. Bud we could test the inner results to.

```kotlin
	.assertValue {it[0].name==""}
	.assertValue{it[1].name=="" }	
```
          
  The final version of our test will be like:
  
```kotlin
@Test
fun returnsAllSavedPlacesWhenGetAll() {
    whenever(dao.getAll()).thenReturn(Flowable.just(getPlaces()))

    viewModel.getAll().test()
            .assertValue {it.size == 2}
	    .assertComplete()
}
```
     


                    
                    
<!--stackedit_data:
eyJoaXN0b3J5IjpbMjAzNDM5NTM0NywtMzc1MTU4NzgxXX0=
-->
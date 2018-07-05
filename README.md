# workshoptw

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

So now we can test the getAll method, that should return 0...N Places. Só we can start testing that values returned by
getAll is empty. But first. Lets look at the getAll in PlaceDAO

 @Query("Select * from Place")
fun getAll(): Flowable<List<Place>>

PlaceDAO::getAll() returns a Flowable, that is a obervable of ReactiveX, it is like a simple Observable<T> plus BackPressure
Strategy, but we can avoid this backpressure thing for now, just becouse we can consume all data produced by getAll method.

Ok, now we know what getAll returns we can start our test. It will be like our first tests, but now we can test the values
returned by the obervable.

Lets start calling viewModel.getAll()
Once it returns a ReactiveX obervable we can make ".test()" and assert some stuffs, like:

viewModel.getAll().test()
    .assertNoValues()
    .assertComplete()
    
If we run this test it will give us a null pointer exception. Thats becouse we don't stub the placeDAO.getAll().
So, when we call placeDAO.getAll() we shoud return a Flowable that respond with a empty value, like:
whenever(dao.getAll()).thenReturn(Flowable.empty()). The final verions of our test will be like:

    @Test
fun emptyWhenAreNoItemsSaved() {
    whenever(dao.getAll()).thenReturn(Flowable.empty())

    viewModel.getAll().test()
            .assertNoValues()
            .assertComplete()
}

Ok, we test the empty case. Lets test when we have item...

Following the same path of code above, we have to stubs the responde of placeDAO.getAll(). But now we shound return
a observable that emmits a item at least.

So... whenever(dao.getAll()).thenReturn(Flowable.just(getPlaces())), the get places method is implemented like

 private fun getPlaces(): List<Place> =
        listOf(Place("", ""), Place("", ""))

Now we can assert that our returned observable contains values.

 viewModel.getAll().test()
            .assertValue {
                it.size == 2 //here we assert 2 itens becouse our getPlaces return 2 places =D, we could make more calls
                //of assert value asserting values inner each item
             }.assertComplete() //just to assert that the emmiting is finished
             
  The final version of our test will be like:
  
      @Test
fun returnsAllSavedPlacesWhenGetAll() {
    whenever(dao.getAll()).thenReturn(Flowable.just(getPlaces()))

    viewModel.getAll().test()
            .assertValue {
                it.size == 2
            }.assertComplete()
}
```
                 
              
    

     
     


                    
                    

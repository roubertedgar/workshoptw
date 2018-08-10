## WorkShop Android

In this workshop we gonna make an application that list your favorite places.

This project gonna be made in 4 steps:
 - [Step-1Step Bar

With this library you can have a step bar that allows you to manage multiple fragment steps in a simple way.

[![](https://gjithub.compack.io/v/roubertedgar/workshoptw/tree/step-1) - Creation of project with main screen that contains the list of your favorite places, creation of form screen to add your favorite places that contains name and description
 - [Step-2](https://github.com/roubertedgar/workshoptw/tree/step-2) - Continuation of [Step-1](https://github.com/roubertedgar/workshoptw/tree/step-1), here we gonna work with [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) to finally have our list of places in the main screen and implements all the things we need to make it works 
 - [Step-3](https://github.com/roubertedgar/workshoptw/tree/step-3) - Until now, we just call the form to add a new place, get the result and put it on the list at the main screen, but we want to persist it, for that we gonna use [Room](https://developer.android.com/topic/libraries/architecture/room). 
 - [Step-4](https://github.com/roubertedgar/workshoptw/tree/step-4) - In this step we decide to use [MVVM](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b), an architecture based on data streaming that helps us to have well defined layers with single resstep-bar.svg)](https://jitpack.io/#roubertedgar/step-bar)    [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Simple%20Step%20Bar-green.svg?style=flat )]( https://android-arsenal.com/details/1/6848 )

----------

![Alt Text](https://media.giphy.com/media/nbOPo5sSZiEmM8YjTc/giphy.gif)

### Add ViewPager and StepBar on layout

The step bar is a viewGroup, so, you can put a close tag like
< /StepBar>
and put some other view inside it.

  ```xml
  <android.support.v4.view.ViewPager
      android:id="@+id/viewPager"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_above="@id/stepBar" />

  <com.steps.StepBar
      android:id="@+id/stepBar"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_alignParentBottom="true" />
  ```

### Implementation of view
Here you have to give a view pager to the step bar.  After complete all steps, the step bar will call the  onComplete of your onCompleteListener. It gives you a bundle with all values of your steps. You can see an simple implementation of it below:

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.step_container_activity)

          val steps = listOf<Step>(FirstStepFragment(), SecondStepFragment())

          viewPager.adapter = SampleStepAdapter(steps, supportFragmentManager)
          stepBar.setViewPager(viewPager)
      }
  ```

### Getting results
To get the results after done all steps, you can use:

```kotlin
  //After compleet the stpes, onComplete will be called
  stepBar.setOnCompleteListener({
      val intent = Intent(this,ResultActivity::class.java)
      intent.putExtras(it)
      startActivity(intent)

  //Where 'it' is a bundle that concat all bundles of 'value' variable, returned by each step
  })
```


### The Fragment
  All the step fragments has to Extends support.v4.app.Fragment and implements Step:

  ```kotlin
  class FirstStepFragment : Fragment(), Step {
  ```

with this, you have to provide a implementation to var value:Bundle, like this:

  ```kotlin
    override var value: Bundle
    get() = getValues()
    set(value) {}

    private fun getValues(): Bundle {
        val bundle = Bundle()
        bundle.putString("keyStepExample", "some value")
        return bundle
    }

  //this value variable will be concat on done all steps and returned to
  //onCompleteListener that you set in stepBar.setOnCompleteListener()
```

And implements invalidateStep method to, like this:

```kotlin
    override fun invalidateStep(invalidate: (isValid: Boolean?) -> Unit{
        invalidate(true) //this step will be ever valid
     }
```

For example, if you want to validate the step checking if some edit text is empty or not, you could do this:

```kotlin
    override fun invalidateStep(invalidate: (isValid: Boolean?) -> Unit) {
        this.invalidate = invalidate

        validate() //This is for validate when step back, it keeps the previous valid step valid
    }

    private fun validate() = invalidate(editText?.text?.let { !it.isEmpty() })
```

### StepAdapter
To work with step bar, your adapter has to extends the StepAdapter and implements getCount and getStep

```kotlin
    override fun getCount(): Int = 3
```
```kotlin
    override fun getStep(position: Int): Step {
        when(ponsibility.
 - [Step-5](https://github.com/roubertedgar/workshoptw/tree/step-5) - On the previous step we applied mvvm architecture, that makes our application easier to test. Só, on fifth step we did some unit test for our app 

**Next steps:** We have plans to implements more steps, like, testing, dependency injection with dagger, http data access... keep calm :)

## Screenstion){
          0-> FirstStep()
          1-> SecondStep()
          2-> ThirdStep()
        }
    }
```

If you give a list of steps to your implementation of step adapter you can make things more simple, like:

```kotlin
    class TestAdapter constructor(val steps: List<Step>, fm: FragmentManager) : StepAdapter(fm) {

        override fun getStep(position: Int): Step = steps[position]

        override fun getCount(): Int = steps.size
    }
```

### Customize
For now you can customize 3 attributes of step bar.

First, import custom attributes
```xml
 xmlns:step="http://schemas.android.com/apk/res-auto"
 ```

***buttons_tint***: this attribute changes the drawable color of back and next step buttons

```xml
<com.steps.StepBar
      android:id="@+id/stepBar"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_alignParentBottom="true"
      step:buttons_tint="@color/colorAccent"/>
```

***done_text_tint***: Using this, you can customize the color of done button text
```xml
<com.steps.StepBar
        android:id="@+id/stepBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        step:done_text_tint="@color/colorPrimary"/>
```

if you don't customize the button text tint, the tint of done text will be the same of back and next step buttons


***done_button_text*** : You can change the text of done button, that comes by default as "DONE"

```xml
<com.steps.StepBar
        android:id="@+id/stepBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        step:done_button_text="Some other text" />
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTgxMzE0MDc2MywxNzk3NDk0MzU0LDE4MT
MxNDA3NjMsMTc5NzQ5NDM1NCwxODEzMTQwNzYzLDE3OTc0OTQz
NTQsMTgxMzE0MDc2MywxNzk3NDk0MzU0LDE4MTMxNDA3NjMsND
MzODc5NzRdfQ==
-->
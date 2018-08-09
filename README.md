# Defining layers of our project with MVVM

We was making a lot of things on the view, making our activities break the single responsibility principle. So, to avoid this principle breaks, we decide to use  an architecture pattern.

Today we have 3 well know architectures that you should read more about and see what is best for your project:
-  [MVC](https://medium.com/upday-devs/android-architecture-patterns-part-1-model-view-controller-3baecef5f2b6)	Model View Controller
-	[MVP](https://medium.com/upday-devs/android-architecture-patterns-part-2-model-view-presenter-8a6faaae14a5) Model View Presenter
-	[MVVM](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b) Model View ViewModel 

In this Step we gonna use MVVM, because we are using ReactiveX to make our asynchronous works, like fetch places from the database. Once MVVM is data stream based, it will make our job easier.

The Movel-View-ViewModel architecture 


<!--stackedit_data:
eyJoaXN0b3J5IjpbMjM3MjU2MDYyLDI4MDc4ODgzOV19
-->
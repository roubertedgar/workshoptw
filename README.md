# Defining layers of our project with MVVM

We was making a lot of things on the view, making our activities break the single responsibility principle. So, to avoid this principle breaks, we decide to use  an architecture pattern.

Today we have 3 well know architectures that you should read more about and see what is best for your project:
-  [MVC](https://medium.com/upday-devs/android-architecture-patterns-part-1-model-view-controller-3baecef5f2b6)	Model View Controller
-	[MVP](https://medium.com/upday-devs/android-architecture-patterns-part-2-model-view-presenter-8a6faaae14a5) Model View Presenter - Can have other variations, like [VIPER](http://luboganev.github.io/blog/clean-architecture-pt1/)
-	[MVVM](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b) Model View ViewModel 

In this Step we gonna use MVVM, because we are using ReactiveX to make our asynchronous works, like fetch places from the database. Once MVVM is data stream based, it will make our job easier.

**The Movel-View-ViewModel architecture**
-   The  View— that informs the ViewModel about the user’s actions
-   The  ViewModel — exposes streams of data relevant to the View
-   The  DataModel — abstracts the data source. The ViewModel works with the DataModel to get and save the data.

While on MVP architecture we have a two way interface that makes the View knows the presenter and the Presenter knows the view, the ViewModel (Like the presenter) of MVVM just expose streams to the view, so, the ViewModel don't knows the View, avoiding to much knowledge between the layers. You can se a MVVM schema below:

![enter image description here](https://cdn-images-1.medium.com/max/800/0*5mD214cjNXU-V6lf.png)


	


<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE5OTk0NTgxODYsLTk3Mjk0Nzk3LC0xMj
MwMDQxNzY4LDI4MDc4ODgzOV19
-->
Training Project For <b>Clean Code</b> and Implement <b>MVVM</b> Design Architecture

<b>Quick Definition of MVVM</b>

MVVM is one of the architectural patterns which enhances separation of concerns, it allows separating the user interface logic from the business (or the back-end) logic. Its target (with other MVC patterns goal) is to achieve the following principle “Keeping UI code simple and free of app logic in order to make it easier to manage”.

<b>MVVM has mainly the following layers:</b>
1. Model
Model represents the data and business logic of the app. One of the recommended implementation strategies of this layer, is to expose its data through observables to be decoupled completely from ViewModel or any other observer/consumer (This will be illustrated in our MVVM sample app below).
2. ViewModel
ViewModel interacts with model and also prepares observable(s) that can be observed by a View. ViewModel can optionally provide hooks for the view to pass events to the model.
One of the important implementation strategies of this layer is to decouple it from the View, i.e, ViewModel should not be aware about the view who is interacting with.
3. View
Finally, the view role in this pattern is to observe (or subscribe to) a ViewModel observable to get data in order to update UI elements accordingly. (https://proandroiddev.com/mvvm-architecture-viewmodel-and-livedata-part-1-604f50cda1)

![alt text](https://miro.medium.com/max/909/1*BpxMFh7DdX0_hqX6ABkDgw.png)


<h1><b>News App<b></h1>

About feature : 
- Show news on list and endless scroll (for load more)
- News Detail with Swipe page using ViewPager based on ItemList
- Search (same with news requirement)
- Archive / Favorite
- Suggestion for search keyword

Project Requirement : 
- Programming Language : Kotlin
- Design Pattern : MVVM Design Architecture
- Database : ROOM 
- Network : Retrofit2

Additional : 
- Android Navigation
- ViewModel & ViewBinding
- Room livedata

Resources To : 
- https://medium.com/@nandoseptian/mvvm-architecture-component-i-4a6c6d38443c
- https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0
- https://proandroiddev.com/mvvm-architecture-viewmodel-and-livedata-part-1-604f50cda1

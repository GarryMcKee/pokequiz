# Who's That Pokemon?

Who's that Pokemon is an android application that recreates the famous Who's that Pokemon segment of the Pokemon tv show. It fetches all 150 of the original Pokemon and will show a random Pokemon silhouette along with four possible answers, including the correct one. If the user selects correct answer we reveal the Pokemon, add to their score, and move on to the next round with a different Pokemon and set of options.

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/cb60afae-1b94-4895-b3c5-894e261f07d5" alt="Image 1" width="400"/></td>
    <td><img src="https://github.com/user-attachments/assets/d8e05c2a-67b8-4dc8-9243-e79357594521" alt="Image 2" width="400"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/84525437-a9e1-4143-8fdd-fbd44826e57d" alt="Image 3" width="400"/></td>
    <td><img src="https://github.com/user-attachments/assets/d7359733-37bf-4d04-a3fd-36c47f7d434f" alt="Image 4" width="400"/></td>
  </tr>
</table>

# Build the App
To build this app ensure you use the following:
* Android Studio Ladybug
* Java 11
* Android SDK Platform 34+
* Android SDK Platform Tools 34+
* Android SDK BuildTools 34+

If any SDK related requirements are not met, you can download them via the Android Studio SDK manager.
These should be downloaded separately from Android Studio if you wish to only build via command line.
To build a release apk (unsigned) of the application run __./gradlew assembleRelease__ from the root directory of the sources.

# Technical Overview
__Architecture__
MVVM
__Conurrency__
Kotlin Coroutines
__Persistence__
Room and Preference Datstore
__Dependency Injection__
Hilt
__REST API Integration__
Retrofit and OkHTTP
__JSON Marshalling__
Moshi
__Image Loading__
Coil, specifically AsyncImage
__Testing__
JUnit, Mockito and Compose UI


# Principles of Design
The application takes several opinionated approaches in it's implementation, lets go through some of them.

## PokeAPI restrictions
The PokeAPI (https://pokeapi.co/ ) is a great free resource that allows clients to query it's database for all kinds of Pokemon information. For Pokemon data specifically it uses a data url concept which links individual items in a Pokemon list to the detail call for that specific Pokemon, the detail required in this case is mainly the official-artwork url. 
We make use of two endpoints to get this information
* Get list of Pokemon
	* https://pokeapi.co/api/v2/pokemon?limit=150&offset=0
	* This endpoint allows us to get a list of all pokemon with an option limit and offset
	* The API lists pokemon in their canonical order (Bulbasaur first etc...), this natural ordering is used in the application
	* The offset lets us off set against the limit to get a difference set of Pokemon
* Get Pokemon Detail by Number
	* https://pokeapi.co/api/v2/pokemon/1/
	* This endpoint gives us access to pokemon details (type, location etc...)
	* We will use it to get the official-artwork url

To this end if we want our app to have access all 150 original Pokemon, and the image url for all of these Pokemon, we will need to call both the /pokemon endpoint as well as the Pokemon/{pokemon number} endpoint for each Pokemon we need information on. This will result in a number of API calls, to mitigate this requirement we will implement a caching strategy
## Persistence
### Database Storage
The Room database is used for storing complex data and lists of data. In our case we use it store the list of Pokémon and their relevant details as well as the round data. In the case of the Pokémon which require repeating rows of data, this is a relatively easy decision, but for the round data, there is a call to be made. The Round itself is a single row entity and could in theory be easily stored as serialised object. Considering how we already require room though it is straightforward to model the data as a table instead.
### Key Value Storage
For the score, which is a simple integer I opted to create a key value storage mechanism backed by Preference Data store. The score is a very simple piece of persisted data that is only ever checked, then incremented.
## Caching
Due to the nature of the API I decided to take a caching approach that would reduce the need for extra calls over time. During the course of the quiz, resources will be saved meaning the app will improve in network performance over time, eventually not requiring any API calls at all. We could have done an upfront load of all Pokemon details but that would require 151 separate calls (and we've gotta cache them all)
### Image Caching
To load images we use Coil's AsyncImage composable: https://coil-kt.github.io/coil/compose/#asyncimage.
Coil. Coil's default behavior with image urls, amongst other image types is to cache meaning if the same Pokemon occurs twice it will be able to show the image without another network call. Coil uses both persistent and in memory caching but the exact nature of this mechanism is outside the scope of this readme.
### Data Caching
As state previously the main detail we need is the Pokemon's imageUrl, to avoid having to look this up each time, the master Pokemon table includes a field for image url. When the initial call to get the Pokemon, and their dataUrls is made, we store the data we currently have, however we leave a column available to store the imageUrl later. When a Pokemon is retrieved from the database to display in a round, we will check if the imageUrl is already available in the database, otherwise we will fetch it using the dataUrl and then store for later use, this means that each Pokemon should only need one API call for it's details per install of the application.
## REST HTTP Client
The REST communication of this application is relatively simple as it requires not authentication. I opted for Retrofit with OKHttp3 as it is a battle tested, and well documented combination of libraries for modeling and communicating with RESTful HTTP resources. To marshall the JSON responses from the API I used Moshi and the __JSONClass()__ annotation to generate adapters.

## State Based UI
The UI is modeled as a finite state machine, at any given point, the screen's round can be in a 
* Ready State 
	* A Pokemon to guess
	* A list of Pokemon options
	* A round state (Correct, Incorrect, Unanswered)
* Loading State
	* The app is currently loading either the initial round including getting all Pokemon or is loading the details for Pokemon in the round
* Error State
	* The app has encountered an error while loading a round and should allow the user to try again

To allow the user to have a score, it is stored "parallel" to the round as the users score persists despite loading, ready or error states.
A finite list of states allows the screen and view model to be tested in an easier fashion as we can generally assert that for given states, certain UI components are shown, and for given data or actions certain states are emitted
The emission of states themselves are handled by using CoRoutine flows, specifically using a __StateFlow__ which is a hot flow that never completes and exposes it's current value at any given time. This makes it a great choice for modelling the flow of state throughout the lifecycle of a composable or screen.
## Reusable UI
The Screen itself is made of smaller components, themselves made of other components. This allows for reuse across and application but also for easier multi form factor support.
The app currently supports landscape and portrait mode for example and this is accomplished essentially by laying out the two main components (The __PokemonCard__ with image and result and the __QuizOptions__ which is a two column list of buttons) either in a column or a row. There is much more that can be done for multi form factor support including resource qualified dimensions for screen sizes and/or densities but all these approaches are helped my reusable UI components
## Theming
The application generally relies on Material themes for Typography and Colours. If we wanted a more elaborate UI we can override the typography, colours and other themes or replace them entirely. This would often be done in conjunction with design system maintained by a designer.

## Concurrency
I often opt to declare dispatcher logic verbosely at the the call site, as opposed to some approaches which add it further down  the layers of an app, such as in a repository managing a network API.
While this can mean that usually our view models contain much __withContext__ calls it allows us to inject our dispatchers in a single place, and see clearly when thread switches occur making debugging of concurrent operations easier.
Dispatchers themselves are provided via a __DispatcherProvider__ interface that makes testing concurrent code much easier.

```
fun loadQuizRoundData() {  
    viewModelScope.launch(dispatcherProvider.main()) {  
  
  _quizScreenUIState.update { currentState ->  
  currentState.copy(quizRoundState = Loading)  
        }  
  
  val newQuizRoundDataResult = withContext(dispatcherProvider.io()) {  
  runCatching {  
  val quizRoundState = getPokemonQuizRoundDataUseCase.execute()  
                val currentScore = getScoreUseCase.execute()  
                _quizScreenUIState.value.copy(  
                    quizRoundState = QuizRoundDataReady(  
                        quizRoundState,  
                        Unanswered  
  ), score = currentScore  
  )  
            }  
 }  
  newQuizRoundDataResult.fold(  
            onSuccess = { state ->  
  _quizScreenUIState.emit(state)  
            },  
            onFailure = { error ->  
  onGetNewRoundDataError(error)  
            }  
  )  
  
    }  
}
```
## Clean Architecture
While the application is simple in concept, for the purposes of demonstrating architecture concepts I opted for a Clean architecture inspired approach. In this case this means:
* Packaging our classes first by feature, then by layer
* Grouping functionality as 
	* UI (User interface and presentation)
	* Domain (Business logic)
	* Data (Remote and local data access )

Adhering to Clean Architecture gives many benefits, namely a high level of separation of concerns as well as generally encouraging smaller classes that do less. It also comes with much overhead and can result in boilerplate code like so called "Anemic" classes. 
	There is many different approaches to this issue like simply "collapsing" the dependencies if a class is anemic, e.g, allowing the ViewModel to depend on a repository directly if a use case would do nothing put pass through calls. In this case I've opted demonstrate both approaches.
	 For example the __FetchPokemonUseCaseImpl__ does little but pass through to the __PokemonRepository.fetchPokemon()__ method giving it little utility, however it __does__ enforce the architecture conventions:
```
class FetchPokemonUseCaseImpl(private val pokemonRepository: PokemonRepository) :  
	    FetchPokemonUseCase {  
	    /*  
	 An example of an anemic use case, this may or may not be appropriate given architecture conventions */  override suspend fun execute() {  
	        pokemonRepository.fetchPokemon()  
	    }  
	  
	}
```
On the other hand if we prefer brevity and avoiding these classes when not required we can "skip" redundant layers like in the case of the __GetScoreUseCaseImpl__ class which relies directly on our __KeyValueStorage__ class. In pure clean architecture we would avoid this by instead using a Repository interface defined by the domain layer and implemented in the data layer, but in this case we allow the domain layer to rely on the data layer directly for the purposes of less redundant code. We could also allow the KeyValueStorage to implement a repository in and of itself while also serving other interfaces adhering to the Interface Segregation principle.
## Error Handling
I'm a believer generally that error handling be as specific as possible, in our case I have modeled a specific type of error that can occur if we get a status code at all from the API calls, the __FetchPokemonException__. 
When an error is thrown in the data layer, we want to throw it up such that it eventually can be handled either in the domain layer or UI layer as is appropriate. In our case we want to show the user a message from the API if it returned an error. If the exception is of some other kind, we show a generic message. This could be expanded such that certain exceptions invoke different functionality entirely (like offline mode) or can be intercepted earlier (like retry authentication). If we were to encounter exceptions in other aspects of the app, like in a database call we may not wish to handle them at all as they may constitute a bug in  database schemas (nullable fields that should be non null etc...) and these may simply crash the app or be handled by a global exception handler. In any case we do not wish to put the application in a state that we cannot account for.
### Coil Image Loading Errors
As an interesting variation on error handling, if Coil, in our UI layer cannot load the Pokemon image, we instruct the Viewmodel to emit an error, as the image itself is required for the app to function. This is done using the __AsyncImage__'s __onError__ callback This is an example of a UI driven intent updating the state of the application

    AsyncImage(  
        model = quizRoundState.pokemonQuizRoundData.pokemonToGuess.imageUrl,  
        contentDescription = null,  
        modifier = Modifier  
      .fillMaxWidth()  
            .weight(1f),  
        colorFilter = if (quizRoundState.quizAnswerState is Unanswered) {  
            ColorFilter.tint(Color.Black)  
        } else {  
            null  
      },  
        onError = {  
      onImageLoadError()  
        }  
    )

## Testing
While the app does not currently have any end to end UI Driven automation tests, I did write a collection of tests that cover the different types of _unit_ tests an application like this might have.
### JUnit JVM Tests
For any classes that are pure Kotlin/Java testing is relatively simple. If we have decoupled their dependencies appropriately we can easily mock these dependencies. In these sorts of unit tests we will focus on public methods either returning a specific value under certain mocked dependencies, or verifying that these public methods caused mocks to be interacted with in an appropriate fashion. Ensuring the classes don't contain Android framework dependencies allows us to run them on any machine's JVM meaning quick and portable test suites.
### View Model Tests
While Android View Models can be tested via JVM tests they often have certain unique requirements as they often encapsulate streams of data as Kotlin flows. In our application we generally test only the end state of these flows, but we use Test CoRoutine Dispatches to ensure that these flows complete in their entirety before we interrogate their end state. We can also develop these tests further by asserting states are emitted in a specific order, and we can use libraries like Turbine to enhance our ability to do so.
### Compose UI Tests
Compose UI as a paradigm turns user interfaces into sets of function calls. This means can view our user interfaces, and their components as a set of functions with certain state. The issue with testing these functions is that they require an Android device to run as they rely on the framework itself. These means they must be written as instrumented tests, and in Compose' case, must also use a ComposeTestRule to host the composable code on screen while they are tested. This boilerplate aside we can test the UI using the test rule to find our UI elements, interact with them and assert the results such as visibility in certain states or invocation of certain methods.
### Database Tests
Room allows us to configure DAO classes that represent functionality against our database tables. Generally the methods we create here are a mix of manual SQL queries and generated SQL queries driven by Rooms annotations (Like @Insert, @Update). We can use Room's in memory database builder to create an instance of our database and run our queries to validate that our queries act on a real database as we would expect.

# Further Work
## UI and Custom Theming
The app could have more elaborate UI work completed and could rely less on the Material themes and opt for a more custom and bespoke visual design. 
The loading state itself is also relatively simple and could have a more friendly design rather than just a progress indicator and text.
We could also add more dynamic scaling and dimension qualifiers to enable the app to support a broader range of form factors.

## Delegate State Flow to UseCases
Currently the __QuizScreenViewModel__ is doing a lot of work orchestrating state flows and combing data from the domain layer. Some of this work could be delegated further down to the use cases and the ViewModel could instead focus on just orchestrating the streams themselves and mapping the data the the UI layer as required. This would make testing the flow of state in the ViewModel a simpler task. This would likely add more flow based architecture further down the line of the use cases and repositories, which currently just use suspend functions

## Test Coverage
The app could aim for 100% test coverage and only lacks it now for the sake of brevity. We also lack integration, screenshot and end to end UI driven automation tests. These sorts of test can be useful for regression testing efforts as well as for covering complex interactions between classes.

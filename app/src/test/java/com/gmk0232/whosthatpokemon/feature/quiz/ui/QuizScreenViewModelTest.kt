package com.gmk0232.whosthatpokemon.feature.quiz.ui

import com.gmk0232.whosthatpokemon.common.dispatcher.TestDispatcherProvider
import com.gmk0232.whosthatpokemon.feature.quiz.data.repository.FetchPokemonException
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetScoreUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoundState.Error
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

val testQuizRoundData = PokemonQuizRoundData(
    pokemonToGuess = Pokemon("Pikachu", 23, imageUrl = ""), pokemonOptions = listOf(
        Pokemon("Rhyhorn", 21, ""),
        Pokemon("Porygon", 44, ""),
        Pokemon("Charmander", 42, ""),
        Pokemon("Pikachu", 23, "")

    )
)
val testQuizScreenUIState = QuizScreenUIState(
    quizRoundState = QuizRoundState.QuizRoundDataReady(
        pokemonQuizRoundData = PokemonQuizRoundData(
            pokemonToGuess = Pokemon("Pikachu", 23, imageUrl = ""), pokemonOptions = listOf(
                Pokemon("Rhyhorn", 21, ""),
                Pokemon("Porygon", 44, ""),
                Pokemon("Charmander", 42, ""),
                Pokemon("Pikachu", 23, "")

            )
        ),
        QuizAnswerState.Unanswered
    ), score = 77
)

/*
It's a little difficult to test all the state in the view model, particularly the loading state
Libraries like Turbine might help here but this probably betrays the fact the view model is doing a bit too much
We could probably put the UseCases to task a bit more and let them emit flows to make this a bit easier
 */
class QuizScreenViewModelTest {
    private val getPokemonQuizRoundDataUseCase: GetPokemonQuizRoundDataUseCase = mock()
    private val determineCorrectPokemonSelectedUseCase: DetermineCorrectPokemonSelectedUseCase =
        mock()
    private val getScoreUseCase: GetScoreUseCase = mock()

    private lateinit var quizScreenViewModel: QuizScreenViewModel

    @Before
    fun setup() {
        quizScreenViewModel = QuizScreenViewModel(
            getPokemonQuizRoundDataUseCase,
            determineCorrectPokemonSelectedUseCase,
            getScoreUseCase,
            TestDispatcherProvider()
        )
    }

    @Test
    fun `load quiz round data returns Unanswered Quiz Screen UI State`() = runTest {
        `when`(getPokemonQuizRoundDataUseCase.execute()).thenReturn(testQuizRoundData)
        `when`(getScoreUseCase.execute()).thenReturn(77)

        quizScreenViewModel.loadQuizRoundData()

        assert(quizScreenViewModel.quizScreenUIState.value == testQuizScreenUIState)
    }

    @Test
    fun `load quiz round data returns Error state with API message when fetching round data fails with FetchPokemonException`() =
        runTest {
            doThrow(FetchPokemonException("401: Test"))
                .`when`(getPokemonQuizRoundDataUseCase).execute()

            `when`(getScoreUseCase.execute()).thenReturn(77)
            quizScreenViewModel.loadQuizRoundData()

            val expectedState = QuizScreenUIState(
                quizRoundState = QuizRoundState.Error(
                    "401: Test"
                ), -1
            )

            assert(quizScreenViewModel.quizScreenUIState.value == expectedState)
        }

    @Test
    fun `load quiz round data returns Error state with generic message when fetching round data fails with Unsupported exceptions`() =
        runTest {
            doThrow(RuntimeException("Test"))
                .`when`(getPokemonQuizRoundDataUseCase).execute()

            `when`(getScoreUseCase.execute()).thenReturn(77)
            quizScreenViewModel.loadQuizRoundData()

            val expectedState = QuizScreenUIState(
                quizRoundState = QuizRoundState.Error(
                    "Something went wrong, please try again"
                ), -1
            )

            assert(quizScreenViewModel.quizScreenUIState.value == expectedState)
        }

    @Test
    fun `onPokemonSelected checks answer and emits when correct`() =
        runTest {
            `when`(getPokemonQuizRoundDataUseCase.execute()).thenReturn(testQuizRoundData)
            `when`(getScoreUseCase.execute()).thenReturn(77)

            `when`(
                determineCorrectPokemonSelectedUseCase.execute(
                    Pokemon(
                        "Pikachu",
                        23,
                        imageUrl = ""
                    )
                )
            ).thenReturn(true)

            quizScreenViewModel.loadQuizRoundData()

            quizScreenViewModel.onPokemonSelected(Pokemon("Pikachu", 23, imageUrl = ""))

            val expectedState = QuizScreenUIState(
                quizRoundState = QuizRoundState.QuizRoundDataReady(
                    testQuizRoundData,
                    QuizAnswerState.Correct
                ), 77
            )

            assert(quizScreenViewModel.quizScreenUIState.value == expectedState)
        }

    @Test
    fun `onPokemonSelected checks answer and emits when incorrect`() =
        runTest {
            `when`(getPokemonQuizRoundDataUseCase.execute()).thenReturn(testQuizRoundData)
            `when`(getScoreUseCase.execute()).thenReturn(77)

            `when`(
                determineCorrectPokemonSelectedUseCase.execute(
                    Pokemon(
                        "Pikachu",
                        23,
                        imageUrl = ""
                    )
                )
            ).thenReturn(false)

            quizScreenViewModel.loadQuizRoundData()

            quizScreenViewModel.onPokemonSelected(Pokemon("Pikachu", 23, imageUrl = ""))

            val expectedState = QuizScreenUIState(
                quizRoundState = QuizRoundState.QuizRoundDataReady(
                    testQuizRoundData,
                    QuizAnswerState.Incorrect
                ), 77
            )

            assert(quizScreenViewModel.quizScreenUIState.value == expectedState)
        }

    @Test
    fun `onImageLoadError emits error state`() =
        runTest {
            `when`(getPokemonQuizRoundDataUseCase.execute()).thenReturn(testQuizRoundData)
            `when`(getScoreUseCase.execute()).thenReturn(77)

            quizScreenViewModel.loadQuizRoundData()
            quizScreenViewModel.onImageLoadError()

            val expectedState = QuizScreenUIState(
                quizRoundState = Error("Could not load pokemon image!"), 77
            )

            assert(quizScreenViewModel.quizScreenUIState.value == expectedState)
        }
}
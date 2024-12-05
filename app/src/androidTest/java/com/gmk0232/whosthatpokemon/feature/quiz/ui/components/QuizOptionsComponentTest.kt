package com.gmk0232.whosthatpokemon.feature.quiz.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoundState
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizScreenUIState
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

val testQuizRoundData = PokemonQuizRoundData(
    pokemonToGuess = Pokemon("Pikachu", 23, imageUrl = ""), pokemonOptions = listOf(
        Pokemon("Rhyhorn", 21, ""),
        Pokemon("Porygon", 44, ""),
        Pokemon("Charmander", 42, ""),
        Pokemon("Pikachu", 23, "")

    )
)
val testUnansweredQuizData = QuizScreenUIState(
    quizRoundState = QuizRoundState.QuizRoundDataReady(
        pokemonQuizRoundData = testQuizRoundData,
        QuizAnswerState.Unanswered
    ), score = 77
)

val testCorrectQuizData = QuizScreenUIState(
    quizRoundState = QuizRoundState.QuizRoundDataReady(
        pokemonQuizRoundData = testQuizRoundData,
        QuizAnswerState.Correct
    ), score = 77
)

val testInCorrectQuizData = QuizScreenUIState(
    quizRoundState = QuizRoundState.QuizRoundDataReady(
        pokemonQuizRoundData = testQuizRoundData,
        QuizAnswerState.Incorrect
    ), score = 77
)

private val onPokemonSelected: (Pokemon) -> Unit = mock()

class QuizOptionsComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun quizOptionsRenderAndAreClickableInUnansweredState() {
        composeTestRule.setContent {
            QuizOptions(
                quizData = testQuizRoundData,
                answerState = QuizAnswerState.Unanswered,
                onPokemonSelected = onPokemonSelected,
                isPortrait = true,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("Pikachu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Porygon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rhyhorn").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charmander").assertIsDisplayed()

        composeTestRule.onNodeWithText("Pikachu").performClick()
        composeTestRule.onNodeWithText("Porygon").performClick()
        composeTestRule.onNodeWithText("Rhyhorn").performClick()
        composeTestRule.onNodeWithText("Charmander").performClick()

        testQuizRoundData.pokemonOptions
            .forEach {
                verify(onPokemonSelected, times(1)).invoke(it)
            }
    }

    @Test
    fun quizOptionsRenderAndAreNotClickableInCorrectState() {
        composeTestRule.setContent {
            QuizOptions(
                quizData = testQuizRoundData,
                answerState = QuizAnswerState.Correct,
                onPokemonSelected = onPokemonSelected,
                isPortrait = true,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("Pikachu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Porygon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rhyhorn").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charmander").assertIsDisplayed()

        composeTestRule.onNodeWithText("Pikachu").performClick()
        composeTestRule.onNodeWithText("Porygon").performClick()
        composeTestRule.onNodeWithText("Rhyhorn").performClick()
        composeTestRule.onNodeWithText("Charmander").performClick()

        testQuizRoundData.pokemonOptions
            .forEach {
                verify(onPokemonSelected, never()).invoke(it)
            }
    }

    @Test
    fun quizOptionsRenderAndAreNotClickableInInCorrectState() {
        composeTestRule.setContent {
            QuizOptions(
                quizData = testQuizRoundData,
                answerState = QuizAnswerState.Incorrect,
                onPokemonSelected = onPokemonSelected,
                isPortrait = true,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("Pikachu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Porygon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rhyhorn").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charmander").assertIsDisplayed()

        composeTestRule.onNodeWithText("Pikachu").performClick()
        composeTestRule.onNodeWithText("Porygon").performClick()
        composeTestRule.onNodeWithText("Rhyhorn").performClick()
        composeTestRule.onNodeWithText("Charmander").performClick()

        testQuizRoundData.pokemonOptions
            .forEach {
                verify(onPokemonSelected, never()).invoke(it)
            }

    }

}
package com.gmk0232.whosthatpokemon.feature.quiz.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmk0232.whosthatpokemon.R
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState
import com.gmk0232.whosthatpokemon.feature.quiz.ui.components.PokemonCard
import com.gmk0232.whosthatpokemon.feature.quiz.ui.components.QuizOptions

val testData = QuizScreenUIState(
    quizRoundState = QuizRoundState.QuizRoundDataReady(
        pokemonQuizRoundData = PokemonQuizRoundData(
            pokemonToGuess = Pokemon("Pikachu", 23, imageUrl = ""), pokemonOptions = listOf(
                Pokemon("Rhyhorn", 21, ""),
                Pokemon("Porygon", 44, ""),
                Pokemon("Charmander", 42, ""),
                Pokemon("Pikachu", 23, "")

            )
        ),
        QuizAnswerState.Correct
    ), score = 77
)

@Composable
fun QuizRoute() {
    val quizScreenViewModel: QuizScreenViewModel = hiltViewModel()
    val quizScreenUIState by quizScreenViewModel.quizScreenUIState.collectAsStateWithLifecycle()

    QuizScreen(
        quizScreenUIState = quizScreenUIState,
        onPokemonSelected = quizScreenViewModel::onPokemonSelected,
        onTryAgainSelected = quizScreenViewModel::loadQuizRoundData,
        onImageLoadError = quizScreenViewModel::onImageLoadError
    )
}


//The linter doesn't seem to be having issues with the orientation calculation and thinks it will always be true
@Suppress("KotlinConstantConditions")
@Composable
fun QuizScreen(
    quizScreenUIState: QuizScreenUIState,
    onPokemonSelected: (Pokemon) -> Unit,
    onTryAgainSelected: () -> Unit,
    onImageLoadError: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {

        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isPortrait) {
            Text(
                stringResource(
                    R.string.score_template_string,
                    quizScreenUIState.score
                ),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            when (quizScreenUIState.quizRoundState) {
                is QuizRoundState.QuizRoundDataReady -> {
                    val quizData = quizScreenUIState.quizRoundState
                    PokemonCard(
                        quizData,
                        isPortrait,
                        Modifier.weight(2f),
                        onImageLoadError
                    )
                    Spacer(Modifier.weight(1f))
                    QuizOptions(
                        quizData.pokemonQuizRoundData,
                        answerState = quizScreenUIState.quizRoundState.quizAnswerState,
                        onPokemonSelected,
                        isPortrait,
                        Modifier.weight(2f)
                    )

                }

                QuizRoundState.Loading -> {
                    LoadingComponent()
                }

                is QuizRoundState.Error -> {
                    Column {
                        Text(quizScreenUIState.quizRoundState.message)
                        Button(onClick = onTryAgainSelected) {
                            Text(
                                stringResource(R.string.reload_quiz_button_text),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        } else {
            when (quizScreenUIState.quizRoundState) {
                is QuizRoundState.QuizRoundDataReady -> {
                    Row(Modifier.padding(8.dp)) {
                        val quizData = quizScreenUIState.quizRoundState
                        PokemonCard(quizData, isPortrait, Modifier.weight(2f), onImageLoadError)
                        Spacer(Modifier.weight(1f))
                        Column(Modifier.weight(2f)) {
                            QuizOptions(
                                quizData.pokemonQuizRoundData,
                                answerState = quizScreenUIState.quizRoundState.quizAnswerState,
                                onPokemonSelected,
                                isPortrait,
                                Modifier.weight(2f)
                            )
                            Text(
                                stringResource(
                                    R.string.score_template_string,
                                    quizScreenUIState.score
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                QuizRoundState.Loading -> {
                    LoadingComponent()
                }

                is QuizRoundState.Error -> {
                    Column {
                        Text(quizScreenUIState.quizRoundState.message)
                        Button(onClick = onTryAgainSelected) {
                            Text(
                                stringResource(R.string.reload_quiz_button_text),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun LoadingComponent() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(stringResource(R.string.loading_label))
    }
}


@Preview(
    name = "Quiz Screen - Portrait",
    widthDp = 410,
    heightDp = 890
)
@Composable
fun QuizScreenPreview(
) {
    QuizScreen(testData, {}, {}, {})
}


@Preview(
    name = "Quiz Screen - Landscape",
    widthDp = 890,
    heightDp = 410
)
@Composable
fun QuizScreenLandscapePreview() {
    QuizScreen(testData, {}, {}, {})
}
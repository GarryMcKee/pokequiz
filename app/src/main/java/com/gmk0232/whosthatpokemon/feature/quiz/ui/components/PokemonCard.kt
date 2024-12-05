package com.gmk0232.whosthatpokemon.feature.quiz.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gmk0232.whosthatpokemon.R
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Unanswered
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoundState

@Composable
fun PokemonCard(
    quizRoundState: QuizRoundState.QuizRoundDataReady,
    isPortrait: Boolean = true,
    modifier: Modifier,
    onImageLoadError: () -> Unit
) {
    Card(
        modifier = modifier.then(
            if (isPortrait) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.fillMaxHeight()
            }
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                stringResource(R.string.title_label),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

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

            if (quizRoundState.quizAnswerState == QuizAnswerState.Correct) {
                Column {
                    ResultText(
                        correctPokemonName = quizRoundState.pokemonQuizRoundData.pokemonToGuess.name,
                        isCorrect = true
                    )
                    PokeballAnimation()
                }
            }

            if (quizRoundState.quizAnswerState == QuizAnswerState.Incorrect) {
                Column {
                    ResultText(
                        correctPokemonName = quizRoundState.pokemonQuizRoundData.pokemonToGuess.name,
                        isCorrect = false
                    )
                    PokeballAnimation()
                }
            }
        }
    }
}
package com.gmk0232.whosthatpokemon.feature.quiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData

@Composable
fun QuizOptions(
    quizData: PokemonQuizRoundData,
    onPokemonSelected: (Pokemon) -> Unit,
    isPortrait: Boolean = true,
    modifier: Modifier
) {
    Column(
        modifier = modifier.then(
            if (isPortrait) {
                modifier.fillMaxWidth()
            } else {
                modifier.fillMaxHeight()
            }
        ), verticalArrangement = if (isPortrait) {
            Arrangement.Bottom
        } else {
            Arrangement.Center
        }
    ) {
        quizData.pokemonOptions.chunked(2)
            .forEach { pokemonChoices ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    pokemonChoices.forEach { pokemonChoice ->
                        Button(onClick = {
                            onPokemonSelected(pokemonChoice)
                        }) {
                            Text(
                                pokemonChoice.name,
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
package com.gmk0232.whosthatpokemon.feature.quiz.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gmk0232.whosthatpokemon.R

@Composable
fun ResultText(correctPokemonName: String, isCorrect: Boolean) {
    Text(
        text = if (isCorrect) {
            stringResource(
                R.string.correct_answer_string,
                correctPokemonName
            )
        } else {
            stringResource(R.string.incorrect_answer_string)
        },
        style = MaterialTheme.typography.headlineSmall
    )
}
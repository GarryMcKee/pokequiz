package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun QuizRoute() {
//    val viewModel: GrapherViewModel = hiltViewModel()
//    val grapherScreenUIState by viewModel.grapherScreenUIState.collectAsStateWithLifecycle()

    QuizScreen(QuizScreenUIState(pokemonToGuess = PokemonUIModel("Pikachu", ""), pokemonOptions = listOf(
        PokemonUIModel("Rhyhorn", ""),
        PokemonUIModel("Porygon", ""),
        PokemonUIModel("Charmander", ""),
        PokemonUIModel("Pikachu", "")

    )))
}

@Composable
fun QuizScreen(quizScreenUIState: QuizScreenUIState) {
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))
        Text("Guess the pokemon:")
        Text(quizScreenUIState.pokemonToGuess.name)
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            quizScreenUIState.pokemonOptions.chunked(2)
                .forEach { pokemonChoices ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        pokemonChoices.forEach { pokemonChoice ->
                            Button({}) {
                                Text(pokemonChoice.name)
                            }
                        }
                    }
                }
        }

    }

}

@Preview
@Composable
fun QuizScreenPreview(
) {
    QuizScreen(QuizScreenUIState(pokemonToGuess = PokemonUIModel("Pikachu", ""), pokemonOptions = listOf(
        PokemonUIModel("Rhyhorn", ""),
        PokemonUIModel("Porygon", ""),
        PokemonUIModel("Charmander", ""),
        PokemonUIModel("Pikachu", "")

    )))
}
package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState.Unanswered

val testData = QuizScreenUIState.QuizRoundDataReady(
    pokemonQuizRoundData = PokemonQuizRoundData(
        pokemonToGuess = Pokemon("Pikachu", 23, imageUrl = ""), pokemonOptions = listOf(
            Pokemon("Rhyhorn", 21, ""),
            Pokemon("Porygon", 44, ""),
            Pokemon("Charmander", 42, ""),
            Pokemon("Pikachu", 23, "")

        )
    ),
    Unanswered
)

@Composable
fun QuizRoute() {
    val quizScreenViewModel: QuizScreenViewModel = hiltViewModel()
    val quizScreenUIState by quizScreenViewModel.quizScreenUIState.collectAsStateWithLifecycle()

    QuizScreen(
        quizScreenUIState = quizScreenUIState,
        onPokemonSelected = quizScreenViewModel::onPokemonSelected
    )
}

@Composable
fun QuizScreen(
    quizScreenUIState: QuizScreenUIState,
    onPokemonSelected: (Pokemon) -> Unit
) {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            Alignment.CenterHorizontally
        ) {

            when (quizScreenUIState) {
                is QuizScreenUIState.QuizRoundDataReady -> {
                    Card(
                        modifier = Modifier
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            val quizData = quizScreenUIState.pokemonQuizRoundData

                            Text(
                                "Who's that pokemon?",
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(quizData.pokemonToGuess.name)
                            if (quizScreenUIState.quizRoundState == QuizRoundState.Correct) {
                                Text("Correct!")
                            }

                            if (quizScreenUIState.quizRoundState == QuizRoundState.Incorrect) {
                                Text("Incorrect!")
                            }
                            Spacer(Modifier.height(16.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
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
                                                    Text(pokemonChoice.name)
                                                }
                                            }
                                        }
                                    }
                            }
                        }

                    }

                }

                QuizScreenUIState.Loading -> {
                    Column {
                        CircularProgressIndicator()
                        Text("Loading..")
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
    QuizScreen(testData, {})
}
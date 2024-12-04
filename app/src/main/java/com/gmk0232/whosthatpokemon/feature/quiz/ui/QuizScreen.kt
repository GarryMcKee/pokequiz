package com.gmk0232.whosthatpokemon.feature.quiz.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
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
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background).padding(8.dp)
    ) {

        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isPortrait) {
            Text(
                "Score: 34",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            when (quizScreenUIState) {
                is QuizScreenUIState.QuizRoundDataReady -> {
                    val quizData = quizScreenUIState.pokemonQuizRoundData
                    PokemonCard(quizData, quizScreenUIState, isPortrait, Modifier.weight(2f))
                    Spacer(Modifier.weight(1f))
                    QuizOptions(quizData, onPokemonSelected, isPortrait, Modifier.weight(2f))

                }

                QuizScreenUIState.Loading -> {
                    Column {
                        CircularProgressIndicator()
                        Text("Loading..")
                    }
                }
            }
        } else {
            when (quizScreenUIState) {
                is QuizScreenUIState.QuizRoundDataReady -> {
                    Row(Modifier.padding(8.dp)){
                        val quizData = quizScreenUIState.pokemonQuizRoundData
                        PokemonCard(quizData, quizScreenUIState, isPortrait, Modifier.weight(2f))
                        Spacer(Modifier.weight(1f))
                        Column(Modifier.weight(2f)) {
                            QuizOptions(quizData, onPokemonSelected, isPortrait, Modifier.weight(2f))
                            Text(
                                "Score: 34",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
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
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun QuizOptions(
    quizData: PokemonQuizRoundData,
    onPokemonSelected: (Pokemon) -> Unit,
    isPortrait: Boolean = true,
    modifier: Modifier
) {
    Column(modifier = modifier.then(
        if(isPortrait) {
            modifier.fillMaxWidth()
        } else {
            modifier.fillMaxHeight()
        }
    ), verticalArrangement = if(isPortrait){
        Arrangement.Bottom
    } else{
        Arrangement.Center
    }) {
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

@Composable
private fun PokemonCard(
    quizData: PokemonQuizRoundData,
    quizScreenUIState: QuizScreenUIState.QuizRoundDataReady,
    isPortrait : Boolean = true,
    modifier: Modifier
) {
    Card(
        modifier = modifier.then(if (isPortrait){
            Modifier.fillMaxWidth()
        } else {
            Modifier.fillMaxHeight()
        })
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                "Who's that pokemon?",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            AsyncImage(
                model = quizData.pokemonToGuess.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                colorFilter = if (quizScreenUIState.quizRoundState is Unanswered) {
                    ColorFilter.tint(Color.Black)
                } else {
                    null
                }
            )

            if (quizScreenUIState.quizRoundState == QuizRoundState.Correct) {
                Text("Correct!")
            }

            if (quizScreenUIState.quizRoundState == QuizRoundState.Incorrect) {
                Text("Incorrect!")
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


@Preview(
    name = "Quiz Screen - Landscape",
    showBackground = true,
    widthDp = 891,  // Adjust width for landscape
    heightDp = 411  // Adjust height for landscape
)
@Composable
fun QuizScreenLandscapePreview() {
    QuizScreen(testData, {})
}
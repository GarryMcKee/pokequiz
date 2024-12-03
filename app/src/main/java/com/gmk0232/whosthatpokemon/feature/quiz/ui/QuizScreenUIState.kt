package com.gmk0232.whosthatpokemon.feature.quiz.ui

import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState

sealed class QuizScreenUIState {
    data class QuizRoundDataReady(val pokemonQuizRoundData: PokemonQuizRoundData, val quizRoundState: QuizRoundState) : QuizScreenUIState()
    data object Loading : QuizScreenUIState()
}
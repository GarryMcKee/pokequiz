package com.gmk0232.whosthatpokemon.feature.quiz.ui

import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData

sealed class QuizScreenUIState {
    data class QuizRoundDataReady(val pokemonQuizRoundData: PokemonQuizRoundData) : QuizScreenUIState()
    data object Loading : QuizScreenUIState()
}
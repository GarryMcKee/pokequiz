package com.gmk0232.whosthatpokemon.feature.quiz.ui

import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState

data class QuizScreenUIState(
    val quizRoundState: QuizRoundState,
    val score: Int,
)

sealed class QuizRoundState {
    data class QuizRoundDataReady(
        val pokemonQuizRoundData: PokemonQuizRoundData,
        val quizAnswerState: QuizAnswerState
    ) : QuizRoundState()

    data object Loading : QuizRoundState()
    data class Error(val message: String) : QuizRoundState()
}
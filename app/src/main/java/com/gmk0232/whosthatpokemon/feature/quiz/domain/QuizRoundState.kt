package com.gmk0232.whosthatpokemon.feature.quiz.domain

sealed class QuizRoundState() {
    data object Unanswered : QuizRoundState()
    data object Correct : QuizRoundState()
    data object Incorrect : QuizRoundState()
}
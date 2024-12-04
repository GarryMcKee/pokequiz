package com.gmk0232.whosthatpokemon.feature.quiz.domain

sealed class QuizAnswerState {
    data object Unanswered : QuizAnswerState()
    data object Correct : QuizAnswerState()
    data object Incorrect : QuizAnswerState()
}
package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface SetScoreUseCase {
    suspend fun execute(score: Int)
}
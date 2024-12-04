package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface GetScoreUseCase {
    suspend fun execute() : Int
}
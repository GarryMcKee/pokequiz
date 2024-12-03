package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface GetPokemonQuizRoundDataUseCase {
    suspend fun execute(): PokemonQuizRoundData
}
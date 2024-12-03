package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface DetermineCorrectPokemonSelectedUseCase {
    suspend fun execute(pokemon: Pokemon) : Boolean
}
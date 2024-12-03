package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface DetermineCorrectPokemonSelectedUseCase {
    fun execute(pokemon: Pokemon) : Boolean
}
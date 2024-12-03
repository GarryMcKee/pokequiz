package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface CurrentRoundRepository {
    suspend fun setCurrentRound(selectedPokemonNumber: Int, pokemonNumberOptions: List<Int>)
    suspend fun getCurrentRound(): PokemonQuizRoundData
}
package com.gmk0232.whosthatpokemon.feature.quiz.domain

interface PokemonRepository {
    suspend fun fetchPokemon()
}
package com.gmk0232.whosthatpokemon.feature.quiz.domain

import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonEntity

data class Pokemon(val name: String, val number: Int, val imageUrl: String)

fun PokemonEntity.toPokemon() =
    Pokemon(name = this.name, number = this.pokemonNumber, imageUrl = this.imageUrl)
package com.gmk0232.whosthatpokemon.feature.quiz.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PokemonEntity(
    @PrimaryKey val pokemonNumber: Int,
    @ColumnInfo(name = "pokemon_name") val name: String,
    @ColumnInfo(name = "data_url") val dataUrl: String
)

fun PokemonListResponse.toPokemonEntityList() = this.results
    .mapIndexed { index, pokemonResponse ->
        PokemonEntity(index, name = pokemonResponse.name, dataUrl = pokemonResponse.url)
    }
package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon


@Entity
data class PokemonEntity(
    @PrimaryKey val pokemonNumber: Int,
    @ColumnInfo(name = "pokemon_name") val name: String,
    @ColumnInfo(name = "data_url") val dataUrl: String,
    @ColumnInfo(name = "image_url") val imageUrl: String = ""
)

fun PokemonListResponse.toPokemonEntityList() = this.results
    .mapIndexed { index, pokemonResponse ->
        PokemonEntity(index, name = pokemonResponse.name, dataUrl = pokemonResponse.url)
    }
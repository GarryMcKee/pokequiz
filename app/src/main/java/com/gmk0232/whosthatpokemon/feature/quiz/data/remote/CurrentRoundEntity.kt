package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentRoundEntity(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "pokemon_to_guess") val pokemonToGuess: Int,
    @ColumnInfo(name = "pokemon_choice_1") val pokemonChoice1: Int,
    @ColumnInfo(name = "pokemon_choice_2") val pokemonChoice2: Int,
    @ColumnInfo(name = "pokemon_choice_3") val pokemonChoice3: Int
)

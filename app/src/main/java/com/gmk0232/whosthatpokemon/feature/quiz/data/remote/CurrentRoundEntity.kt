package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData

@Entity
data class CurrentRoundEntity(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "pokemon_to_guess") val pokemonToGuess: Int,
    @ColumnInfo(name = "pokemon_choice_1") val pokemonChoice1: Int,
    @ColumnInfo(name = "pokemon_choice_2") val pokemonChoice2: Int,
    @ColumnInfo(name = "pokemon_choice_3") val pokemonChoice3: Int
)

fun PokemonQuizRoundData.toCurrentRoundEntity(): CurrentRoundEntity {
    return CurrentRoundEntity(
        pokemonToGuess = this.pokemonToGuess.number,
        pokemonChoice1 = pokemonOptions[0].number,
        pokemonChoice2 = pokemonOptions[1].number,
        pokemonChoice3 = pokemonOptions[2].number
    )
}

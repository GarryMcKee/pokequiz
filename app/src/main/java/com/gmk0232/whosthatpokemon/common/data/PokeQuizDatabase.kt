package com.gmk0232.whosthatpokemon.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.CurrentRoundEntity
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonEntity

@Database(entities = [CurrentRoundEntity::class, PokemonEntity::class], version = 1)
abstract class PokeQuizDatabase : RoomDatabase() {
    abstract fun currentRoundDao(): CurrentRoundDao
    abstract fun pokemonDao(): PokemonDao
}
package com.gmk0232.whosthatpokemon.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.CurrentRoundEntity
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.PokemonDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.PokemonEntity

@Database(entities = [CurrentRoundEntity::class, PokemonEntity::class], version = 1)
abstract class PokeQuizDatabase : RoomDatabase() {
    abstract fun currentRoundDao(): CurrentRoundDao
    abstract fun pokemonDao(): PokemonDao
}
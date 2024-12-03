package com.gmk0232.whosthatpokemon.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmk0232.whosthatpokemon.feature.quiz.data.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.CurrentRoundEntity

@Database(entities = [CurrentRoundEntity::class], version = 1)
abstract class PokeQuizDatabase : RoomDatabase() {
    abstract fun currentRoundDao(): CurrentRoundDao
}
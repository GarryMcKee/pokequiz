package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrentRoundDao {
    @Query("SELECT * FROM currentroundentity LIMIT 1")
    suspend fun getCurrentRound(): CurrentRoundEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRound(roundData: CurrentRoundEntity)

}
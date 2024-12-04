package com.gmk0232.whosthatpokemon.feature.quiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/*
As this is just a single item, ProtoData store or some other serialisable type storage might have worked well
As Room is already required however I opted for a single row table approach
 */
@Dao
interface CurrentRoundDao {
    @Query("SELECT * FROM currentroundentity LIMIT 1")
    suspend fun getCurrentRound(): CurrentRoundEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRound(roundData: CurrentRoundEntity)

}
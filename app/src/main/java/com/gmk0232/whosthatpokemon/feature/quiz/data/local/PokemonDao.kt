package com.gmk0232.whosthatpokemon.feature.quiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemonentity where pokemonNumber LIKE :number LIMIT 1")
    fun getPokemonByNumber(number: Int): PokemonEntity

    @Query("SELECT * FROM pokemonentity")
    fun getAllPokemon(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(pokemon: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(pokemon: PokemonEntity)
}
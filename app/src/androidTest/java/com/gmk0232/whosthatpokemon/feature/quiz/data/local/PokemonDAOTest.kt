package com.gmk0232.whosthatpokemon.feature.quiz.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gmk0232.whosthatpokemon.common.data.PokeQuizDatabase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private val testPokemonList =
    listOf(
        PokemonEntity(1, "Bulbasaur", ""),
        PokemonEntity(2, "Charmander", ""),
        PokemonEntity(3, "Squirtle", "")
    )

@RunWith(AndroidJUnit4::class)
class PokemonDAOTest {
    private lateinit var pokemonDao: PokemonDao
    private lateinit var pokeQuizDatabase: PokeQuizDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        pokeQuizDatabase =
            Room.inMemoryDatabaseBuilder(context, PokeQuizDatabase::class.java).build()

        pokemonDao = pokeQuizDatabase.pokemonDao()
    }

    @Test
    fun insertPokemonListInsertsAllPokemonEntities() = runTest {
        pokemonDao.insertPokemon(testPokemonList)
        assert(pokemonDao.getAllPokemon() == testPokemonList)
    }

    @Test
    fun getPokemonByNumberReturnsCorrectRecord() = runTest {
        pokemonDao.insertPokemon(testPokemonList)
        assert(pokemonDao.getPokemonByNumber(2) == PokemonEntity(2, "Charmander", ""))
    }

    @Test
    fun updatePokemonAddsUpdatedInformation() = runTest {
        pokemonDao.insertPokemon(testPokemonList)
        assert(pokemonDao.getPokemonByNumber(3) == PokemonEntity(3, "Squirtle", ""))

        pokemonDao.updatePokemon(PokemonEntity(3, "Squirtle", "testUrl"))
        assert(pokemonDao.getPokemonByNumber(3) == PokemonEntity(3, "Squirtle", "testUrl"))
    }
}
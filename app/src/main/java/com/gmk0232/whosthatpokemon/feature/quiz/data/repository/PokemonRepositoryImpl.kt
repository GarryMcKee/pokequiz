package com.gmk0232.whosthatpokemon.feature.quiz.data.repository

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonAPI
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.PokemonDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.toPokemonEntityList
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonRepository

const val POKEMON_LIMIT = 150
const val POKEMON_OFFSET = 0

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao,
    private val pokemonApi: PokemonAPI
) : PokemonRepository {
    override suspend fun fetchPokemon() {
        val pokemonResponse = pokemonApi.fetchPokemon(POKEMON_LIMIT, POKEMON_OFFSET)

        if (pokemonResponse.isSuccessful) {
            val pokemonEntities = pokemonResponse.body()!!.toPokemonEntityList()
                .map { it.copy(name = it.name.capitalize(Locale.current)) }
            pokemonDao.insertPokemon(pokemonEntities)
        } else {
            throw FetchPokemonException(
                "Error code: ${pokemonResponse.code()} ${
                    pokemonResponse.errorBody()
                        ?.string() ?: "Something went wrong when fetching pokemon"
                }"
            )
        }

    }

    override suspend fun checkPokemonListReady(): Boolean {
        return pokemonDao.getAllPokemon().isNotEmpty()
    }
}

class FetchPokemonException(private val errorMessage: String) : RuntimeException() {
    override val message: String
        get() = errorMessage
}
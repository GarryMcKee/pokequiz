package com.gmk0232.whosthatpokemon.feature.quiz.data

import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonRepository

const val POKEMON_LIMIT = 150
const val POKEMON_OFFSET = 0

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao,
    private val pokemonApi: PokemonAPI
) :
    PokemonRepository {
    override suspend fun fetchPokemon() {
        val pokemonResponse = pokemonApi.fetchPokemon(POKEMON_LIMIT, POKEMON_OFFSET)

        if (pokemonResponse.isSuccessful) {
            val pokemonEntities = pokemonResponse.body()!!.toPokemonEntityList()
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
}

class FetchPokemonException(private val errorMessage: String) : Throwable() {
    override val message: String
        get() = errorMessage
}
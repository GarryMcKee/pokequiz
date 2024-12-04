package com.gmk0232.whosthatpokemon.feature.quiz.data.repository

import com.gmk0232.whosthatpokemon.feature.quiz.data.local.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.CurrentRoundEntity
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonAPI
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.PokemonDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.PokemonEntity
import com.gmk0232.whosthatpokemon.feature.quiz.domain.CurrentRoundRepository
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData
import com.gmk0232.whosthatpokemon.feature.quiz.domain.toPokemon

class CurrentRoundRepositoryImpl(
    private val currentRoundDao: CurrentRoundDao,
    private val pokemonDao: PokemonDao,
    private val pokemonApi: PokemonAPI
) :
    CurrentRoundRepository {

    override suspend fun setCurrentRound(
        selectedPokemonNumber: Int,
        pokemonNumberOptions: List<Int>
    ) {
        val currentRoundEntity = CurrentRoundEntity(
            pokemonToGuess = selectedPokemonNumber,
            pokemonChoice1 = pokemonNumberOptions[1],
            pokemonChoice2 = pokemonNumberOptions[2],
            pokemonChoice3 = pokemonNumberOptions[3]
        )

        currentRoundDao.insertRound(currentRoundEntity)
    }

    override suspend fun getCurrentRound(): PokemonQuizRoundData {
        val currentRound = currentRoundDao.getCurrentRound()
        val pokemonToGuess = getPokemonDetailForNumber(currentRound.pokemonToGuess).toPokemon()
        val pokemonChoice1 = getPokemonDetailForNumber(currentRound.pokemonChoice1).toPokemon()
        val pokemonChoice2 = getPokemonDetailForNumber(currentRound.pokemonChoice2).toPokemon()
        val pokemonChoice3 = getPokemonDetailForNumber(currentRound.pokemonChoice3).toPokemon()

        return PokemonQuizRoundData(
            pokemonToGuess,
            listOf(pokemonToGuess, pokemonChoice1, pokemonChoice2, pokemonChoice3)
        )
    }

    private suspend fun getPokemonDetailForNumber(pokemonNumber: Int): PokemonEntity {
        /*
        If we don't already have the data required for this pokemon from previous calls
        Fetch the data for the specific pokemon, store and then return it
         */
        val pokemonEntity = pokemonDao.getPokemonByNumber(pokemonNumber)
        if (pokemonEntity.imageUrl.isEmpty()) {
            val pokemonDetailResponse = pokemonApi.fetchPokemonDetail(pokemonEntity.dataUrl)
            if (pokemonDetailResponse.isSuccessful) {
                val imageUrl =
                    pokemonDetailResponse.body()!!.sprites.other.officialArtworkUrl.frontDefaultUrl
                val updatedPokemonEntity = pokemonEntity.copy(imageUrl = imageUrl)
                pokemonDao.insertPokemon(updatedPokemonEntity)
                return updatedPokemonEntity
            } else {
                throw FetchPokemonException(
                    "Error code: ${pokemonDetailResponse.code()} ${
                        pokemonDetailResponse.errorBody()
                            ?.string() ?: "Something went wrong when fetching pokemon"
                    }"
                )
            }
        } else {
            return pokemonEntity
        }

    }
}
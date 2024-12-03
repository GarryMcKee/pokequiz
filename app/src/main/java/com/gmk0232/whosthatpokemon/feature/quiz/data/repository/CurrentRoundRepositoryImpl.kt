package com.gmk0232.whosthatpokemon.feature.quiz.data.repository

import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.CurrentRoundEntity
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonAPI
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonDetailResponse
import com.gmk0232.whosthatpokemon.feature.quiz.domain.CurrentRoundRepository
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData

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
        val pokemonToGuessUrl = pokemonDao.getPokemonByNumber(currentRound.pokemonToGuess).dataUrl
        val pokemonOption1 = pokemonDao.getPokemonByNumber(currentRound.pokemonChoice1).dataUrl
        val pokemonOption2 = pokemonDao.getPokemonByNumber(currentRound.pokemonChoice2).dataUrl
        val pokemonOption3 = pokemonDao.getPokemonByNumber(currentRound.pokemonChoice3).dataUrl

        val pokemonDetailUrls =
            listOf(pokemonToGuessUrl, pokemonOption1, pokemonOption2, pokemonOption3)

        val pokemonDetails = pokemonDetailUrls.map {
            getPokemonDetailForUrl(it)
        }

        val pokemonToGuess = Pokemon(
            name = pokemonDao.getPokemonByNumber(currentRound.pokemonToGuess).name,
            number = currentRound.pokemonToGuess,
            imageUrl = ""
        )
        val pokemonChoice1 = Pokemon(
            name = pokemonDao.getPokemonByNumber(currentRound.pokemonChoice1).name,
            number = currentRound.pokemonChoice1,
            imageUrl = ""
        )
        val pokemonChoice2 = Pokemon(
            name = pokemonDao.getPokemonByNumber(currentRound.pokemonChoice2).name,
            number = currentRound.pokemonChoice2,
            imageUrl = ""
        )
        val pokemonChoice3 = Pokemon(
            name = pokemonDao.getPokemonByNumber(currentRound.pokemonChoice3).name,
            number = currentRound.pokemonChoice3,
            imageUrl = ""
        )

        return PokemonQuizRoundData(
            pokemonToGuess,
            listOf(pokemonToGuess, pokemonChoice1, pokemonChoice2, pokemonChoice3)
        )
    }

    private suspend fun getPokemonDetailForUrl(url: String): PokemonDetailResponse {
        val pokemonDetailResponse = pokemonApi.fetchPokemonDetail(url)
        if (pokemonDetailResponse.isSuccessful) {
            return pokemonDetailResponse.body()!!
        } else {
            throw FetchPokemonException(
                "Error code: ${pokemonDetailResponse.code()} ${
                    pokemonDetailResponse.errorBody()
                        ?.string() ?: "Something went wrong when fetching pokemon"
                }"
            )
        }
    }
}
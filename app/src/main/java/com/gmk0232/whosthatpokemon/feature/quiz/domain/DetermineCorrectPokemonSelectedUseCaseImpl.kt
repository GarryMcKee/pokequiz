package com.gmk0232.whosthatpokemon.feature.quiz.domain

import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage

class DetermineCorrectPokemonSelectedUseCaseImpl(
    private val currentRoundRepository: CurrentRoundRepository,
    private val keyValueStorage: KeyValueStorage
) :
    DetermineCorrectPokemonSelectedUseCase {
    override suspend fun execute(pokemon: Pokemon): Boolean {
        val currentRound = currentRoundRepository.getCurrentRound()
        val isCorrect = (currentRound.pokemonToGuess.number == pokemon.number)

        if (isCorrect) {
            val currentScore = keyValueStorage.getInt(SCORE_PREFERENCE_KEY)
            keyValueStorage.setInt(SCORE_PREFERENCE_KEY, currentScore + 1)
        }
        return isCorrect
    }
}
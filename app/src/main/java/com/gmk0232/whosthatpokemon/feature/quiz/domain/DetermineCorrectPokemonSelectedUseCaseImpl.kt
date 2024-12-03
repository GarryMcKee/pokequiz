package com.gmk0232.whosthatpokemon.feature.quiz.domain

class DetermineCorrectPokemonSelectedUseCaseImpl(private val currentRoundRepository: CurrentRoundRepository) :
    DetermineCorrectPokemonSelectedUseCase {
    override suspend fun execute(pokemon: Pokemon): Boolean {
        val currentRound = currentRoundRepository.getCurrentRound()
        return (currentRound.pokemonToGuess.number == pokemon.number)
    }
}
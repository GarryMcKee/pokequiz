package com.gmk0232.whosthatpokemon.feature.quiz.domain

const val MINIMUM_POKEMON_NUMBER = 1
const val MAXIMUM_POKEMON_NUMBER = 150

val possiblePokemonNumbers = (MINIMUM_POKEMON_NUMBER..MAXIMUM_POKEMON_NUMBER).toList()

class GetPokemonQuizRoundDataUseCaseImpl(private val currentRoundRepository: CurrentRoundRepository) :
    GetPokemonQuizRoundDataUseCase {
    override suspend fun execute(): PokemonQuizRoundData {

        val pokemonToGuessNumber = possiblePokemonNumbers.random()

        val pokemonNumberOptions = mutableListOf(pokemonToGuessNumber)
        repeat(3) {
            pokemonNumberOptions.add(getRandomPokemon(pokemonNumberOptions))
        }

        /*
        We could return the round data directly here but to keep the single source of truth flow we'll return it from the DB instead
        If we required this round data to be dynamic this means we'll have less refactoring to do if we converted to a flow for example
         */
        currentRoundRepository.setCurrentRound(pokemonToGuessNumber, pokemonNumberOptions)

        return currentRoundRepository.getCurrentRound()
    }

    private fun getRandomPokemon(currentPokemonNumbers: List<Int>): Int {
        return possiblePokemonNumbers.filter {
            filterPokemonAlreadyAdded(
                currentPokemonNumbers,
                it
            )
        }.random()
    }

    private fun filterPokemonAlreadyAdded(
        currentPokemonNumbers: List<Int>,
        number: Int
    ) = currentPokemonNumbers.contains(number).not()
}
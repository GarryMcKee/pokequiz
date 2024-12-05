package com.gmk0232.whosthatpokemon.feature.quiz.domain

const val MINIMUM_POKEMON_NUMBER = 1
const val MAXIMUM_POKEMON_NUMBER = 150

/*
While figuring out what pokemon to actually show, we only need their number
Lets make a list of possible numbers based on the range of pokemon supported in the quiz

We could also make this more testable by injecting a random number provider
 */
val possiblePokemonNumbers = (MINIMUM_POKEMON_NUMBER..MAXIMUM_POKEMON_NUMBER).toList()

class GetPokemonQuizRoundDataUseCaseImpl(
    private val currentRoundRepository: CurrentRoundRepository,
    private val pokemonRepository: PokemonRepository
) :
    GetPokemonQuizRoundDataUseCase {
    override suspend fun execute(): PokemonQuizRoundData {

        //If we don't have any pokemon data loaded yet, load it first then create the round
        return if (pokemonRepository.checkPokemonListReady()) {
            createQuizRound()
        } else {
            pokemonRepository.fetchPokemon()
            createQuizRound()
        }

    }

    private suspend fun createQuizRound(): PokemonQuizRoundData {
        //Get a random pokemon number the user will guess
        val pokemonNumberToGuess = possiblePokemonNumbers.random()

        /*
        Create a list of 4 options for the user to select, including the pokemon to guess
         */
        val pokemonNumberOptions = mutableListOf(pokemonNumberToGuess)
        repeat(3) {
            pokemonNumberOptions.add(getRandomPokemon(pokemonNumberOptions))
        }

        /*
        Store the current round data in the database
         */
        currentRoundRepository.setCurrentRound(
            pokemonNumberToGuess,
            pokemonNumberOptions
        )

        val currentRound = currentRoundRepository.getCurrentRound()
        val shuffledRound =
            currentRound.copy(pokemonOptions = currentRound.pokemonOptions.shuffled())

        return shuffledRound
    }

    private fun getRandomPokemon(currentPokemonNumbers: List<Int>): Int {
        /*
        Get a random pokemon number that we haven't already gotten
         */
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
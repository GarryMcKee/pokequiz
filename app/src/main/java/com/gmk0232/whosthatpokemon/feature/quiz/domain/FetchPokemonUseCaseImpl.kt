package com.gmk0232.whosthatpokemon.feature.quiz.domain

class FetchPokemonUseCaseImpl(private val pokemonRepository: PokemonRepository) :
    FetchPokemonUseCase {
        /*
        An example of an anemic use case
         */
    override suspend fun execute() {
        pokemonRepository.fetchPokemon()
    }

}
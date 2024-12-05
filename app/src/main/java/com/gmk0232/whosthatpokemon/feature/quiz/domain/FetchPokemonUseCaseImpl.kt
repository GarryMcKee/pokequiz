package com.gmk0232.whosthatpokemon.feature.quiz.domain

class FetchPokemonUseCaseImpl(private val pokemonRepository: PokemonRepository) :
    FetchPokemonUseCase {
    /*
    An example of an anemic use case, this may or may not be appropriate given architecture conventions
     */
    override suspend fun execute() {
        pokemonRepository.fetchPokemon()
    }

}
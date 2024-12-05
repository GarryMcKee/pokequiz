package com.gmk0232.whosthatpokemon.feature.quiz.domain

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class FetchPokemonUseCaseImplTest {
    private val pokemonRepository : PokemonRepository = mock()

    private lateinit var fetchPokemonUseCaseImpl: FetchPokemonUseCaseImpl

    @Before
    fun setup() {
        fetchPokemonUseCaseImpl = FetchPokemonUseCaseImpl(pokemonRepository)
    }

    @Test
    fun `Fetch Pokemon Use Case requests repository to fetch pokemon`() = runTest{
        fetchPokemonUseCaseImpl.execute()
        verify(pokemonRepository, times(1)).fetchPokemon()
    }
}
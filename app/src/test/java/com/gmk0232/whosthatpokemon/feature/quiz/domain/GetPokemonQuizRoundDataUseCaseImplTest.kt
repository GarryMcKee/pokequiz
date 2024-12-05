package com.gmk0232.whosthatpokemon.feature.quiz.domain

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

val testQuizRoundData =
    PokemonQuizRoundData(
        pokemonToGuess = Pokemon("Pikachu", 23, imageUrl = ""), pokemonOptions = listOf(
            Pokemon("Rhyhorn", 21, ""),
            Pokemon("Porygon", 44, ""),
            Pokemon("Charmander", 42, ""),
            Pokemon("Pikachu", 23, "")

        )
    )

/*
As noted in the class itself, this test could be improved by also mocking the random number mechanism.
This would allow us to also assert the correct number are added to the round
 */
class GetPokemonQuizRoundDataUseCaseImplTest {
    private val currentRoundRepository: CurrentRoundRepository = mock()
    private val pokemonRepository: PokemonRepository = mock()

    private lateinit var getPokemonQuizRoundDataUseCaseImpl: GetPokemonQuizRoundDataUseCaseImpl

    @Before
    fun setup() {
        getPokemonQuizRoundDataUseCaseImpl =
            GetPokemonQuizRoundDataUseCaseImpl(currentRoundRepository, pokemonRepository)
    }

    @Test
    fun `When get round data is called for first time, full pokemon list is fetched and stored before round data returned`() =
        runTest {
            `when`(pokemonRepository.checkPokemonListReady()).thenReturn(false)
            `when`(currentRoundRepository.getCurrentRound()).thenReturn(testQuizRoundData)

            val result = getPokemonQuizRoundDataUseCaseImpl.execute()

            verify(pokemonRepository, times(1)).fetchPokemon()
            assert(result == testQuizRoundData)
        }

    @Test
    fun `When get round data is called for subsequent rounds, full pokemon list is not fetched and stored before round data returned`() =
        runTest {
            `when`(pokemonRepository.checkPokemonListReady()).thenReturn(true)
            `when`(currentRoundRepository.getCurrentRound()).thenReturn(testQuizRoundData)

            val result = getPokemonQuizRoundDataUseCaseImpl.execute()

            verify(pokemonRepository, never()).fetchPokemon()
            assert(result == testQuizRoundData)
        }
}
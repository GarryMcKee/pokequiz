package com.gmk0232.whosthatpokemon.feature.quiz.domain

import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

class DetermineCorrectPokemonSelectedUseCaseImplTest {
    private val currentRoundRepository: CurrentRoundRepository = mock()
    private val keyValueStorage: KeyValueStorage = mock()

    private lateinit var determineCorrectPokemonSelectedUseCaseImpl: DetermineCorrectPokemonSelectedUseCaseImpl

    @Before
    fun setup() {
        determineCorrectPokemonSelectedUseCaseImpl =
            DetermineCorrectPokemonSelectedUseCaseImpl(currentRoundRepository, keyValueStorage)
    }

    @Test
    fun `When correct pokemon selected return correct and increment score`() = runTest {
        `when`(currentRoundRepository.getCurrentRound()).thenReturn(
            PokemonQuizRoundData(Pokemon("Test", 77, ""), emptyList())
        )

        `when`(keyValueStorage.getInt(SCORE_PREFERENCE_KEY)).thenReturn(5)


        val result = determineCorrectPokemonSelectedUseCaseImpl.execute(Pokemon("Test", 77, ""))
        assert(result)
        verify(keyValueStorage).getInt(SCORE_PREFERENCE_KEY)
        verify(keyValueStorage).setInt(SCORE_PREFERENCE_KEY, 6)
    }

    @Test
    fun `When correct pokemon selected return incorrect and don't increment score`() = runTest {
        `when`(currentRoundRepository.getCurrentRound()).thenReturn(
            PokemonQuizRoundData(Pokemon("Test", 77, ""), emptyList())
        )

        `when`(keyValueStorage.getInt(SCORE_PREFERENCE_KEY)).thenReturn(5)


        val result = determineCorrectPokemonSelectedUseCaseImpl.execute(Pokemon("Test", 99, ""))
        assert(result.not())
        verify(keyValueStorage, never()).getInt(SCORE_PREFERENCE_KEY)
        verify(keyValueStorage, never()).setInt(SCORE_PREFERENCE_KEY, 6)
    }
}
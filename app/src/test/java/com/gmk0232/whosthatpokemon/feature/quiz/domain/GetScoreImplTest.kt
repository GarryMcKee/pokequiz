package com.gmk0232.whosthatpokemon.feature.quiz.domain

import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class GetScoreImplTest {
    private val keyValueStorage: KeyValueStorage = mock()

    private lateinit var getScoreUseCaseImpl: GetScoreUseCaseImpl

    @Before
    fun setup() {
        getScoreUseCaseImpl = GetScoreUseCaseImpl(keyValueStorage)
    }

    @Test
    fun `Get Score Use Case returns the current score from storage`() = runTest {
        `when`(keyValueStorage.getInt(SCORE_PREFERENCE_KEY)).thenReturn(77)

        assert(getScoreUseCaseImpl.execute() == 77)
    }
}
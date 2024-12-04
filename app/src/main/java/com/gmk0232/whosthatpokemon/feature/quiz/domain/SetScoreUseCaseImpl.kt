package com.gmk0232.whosthatpokemon.feature.quiz.domain

import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage

class SetScoreUseCaseImpl(private val keyValueStorage: KeyValueStorage) : SetScoreUseCase {
    override suspend fun execute(score: Int) {
        keyValueStorage.setInt(SCORE_PREFERENCE_KEY, score)
    }
}
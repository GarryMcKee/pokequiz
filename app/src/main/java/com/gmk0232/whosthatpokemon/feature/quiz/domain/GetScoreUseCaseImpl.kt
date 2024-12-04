package com.gmk0232.whosthatpokemon.feature.quiz.domain

import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage

const val SCORE_PREFERENCE_KEY = "scorePreferenceKey"

class GetScoreUseCaseImpl(private val keyValueStorage: KeyValueStorage) : GetScoreUseCase {
    override suspend fun execute(): Int = keyValueStorage.getInt(SCORE_PREFERENCE_KEY)
}
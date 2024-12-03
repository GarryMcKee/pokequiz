package com.gmk0232.whosthatpokemon.feature.quiz.di

import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class QuizModule {
    @Provides
    fun GetPokemonQuizRoundDataUseCase(): GetPokemonQuizRoundDataUseCase {
        return GetPokemonQuizRoundDataUseCaseImpl()
    }
}
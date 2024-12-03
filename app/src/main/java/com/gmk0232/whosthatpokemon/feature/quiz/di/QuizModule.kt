package com.gmk0232.whosthatpokemon.feature.quiz.di

import com.gmk0232.whosthatpokemon.common.data.PokeQuizDatabase
import com.gmk0232.whosthatpokemon.feature.quiz.data.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.CurrentRoundRepositoryImpl
import com.gmk0232.whosthatpokemon.feature.quiz.domain.CurrentRoundRepository
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCaseImpl
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
    fun providePokemonQuizRoundDataUseCase(currentRoundRepository: CurrentRoundRepository): GetPokemonQuizRoundDataUseCase {
        return GetPokemonQuizRoundDataUseCaseImpl(currentRoundRepository)
    }

    @Provides
    fun provideCurrentRoundDao(pokeQuizDatabase: PokeQuizDatabase): CurrentRoundDao {
        return pokeQuizDatabase.currentRoundDao()
    }

    @Provides
    fun provideCurrentRoundRepository(currentRoundDao: CurrentRoundDao): CurrentRoundRepository {
        return CurrentRoundRepositoryImpl(currentRoundDao)
    }

    @Provides
    fun provideDetermineCorrectPokemonSelectedUseCase(): DetermineCorrectPokemonSelectedUseCase {
        return DetermineCorrectPokemonSelectedUseCaseImpl()
    }
}
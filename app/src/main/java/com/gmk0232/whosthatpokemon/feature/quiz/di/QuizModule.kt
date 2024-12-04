package com.gmk0232.whosthatpokemon.feature.quiz.di

import com.gmk0232.whosthatpokemon.common.data.PokeQuizDatabase
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.CurrentRoundDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.PokemonDao
import com.gmk0232.whosthatpokemon.feature.quiz.data.remote.PokemonAPI
import com.gmk0232.whosthatpokemon.feature.quiz.data.repository.CurrentRoundRepositoryImpl
import com.gmk0232.whosthatpokemon.feature.quiz.data.repository.PokemonRepositoryImpl
import com.gmk0232.whosthatpokemon.feature.quiz.domain.CurrentRoundRepository
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCaseImpl
import com.gmk0232.whosthatpokemon.feature.quiz.domain.FetchPokemonUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.FetchPokemonUseCaseImpl
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCaseImpl
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetScoreUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetScoreUseCaseImpl
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class QuizModule {
    @Provides
    fun providePokemonQuizRoundDataUseCase(
        currentRoundRepository: CurrentRoundRepository,
        pokemonRepository: PokemonRepository
    ): GetPokemonQuizRoundDataUseCase {
        return GetPokemonQuizRoundDataUseCaseImpl(
            currentRoundRepository,
            pokemonRepository = pokemonRepository
        )
    }

    @Provides
    fun provideCurrentRoundDao(pokeQuizDatabase: PokeQuizDatabase): CurrentRoundDao {
        return pokeQuizDatabase.currentRoundDao()
    }

    @Provides
    fun providePokemonDAO(pokeQuizDatabase: PokeQuizDatabase): PokemonDao {
        return pokeQuizDatabase.pokemonDao()
    }

    @Provides
    fun provideCurrentRoundRepository(
        currentRoundDao: CurrentRoundDao,
        pokemonDao: PokemonDao,
        pokemonAPI: PokemonAPI
    ): CurrentRoundRepository {
        return CurrentRoundRepositoryImpl(currentRoundDao, pokemonDao, pokemonAPI)
    }

    @Provides
    fun provideDetermineCorrectPokemonSelectedUseCase(
        currentRoundRepository: CurrentRoundRepository,
        keyValueStorage: KeyValueStorage
    ): DetermineCorrectPokemonSelectedUseCase {
        return DetermineCorrectPokemonSelectedUseCaseImpl(currentRoundRepository, keyValueStorage)
    }

    @Provides
    fun provideFetchPokemonRepository(
        pokemonDao: PokemonDao,
        pokemonAPI: PokemonAPI
    ): PokemonRepository {
        return PokemonRepositoryImpl(pokemonDao, pokemonAPI)
    }


    @Provides
    fun provideFetchPokemonUseCase(pokemonRepository: PokemonRepository): FetchPokemonUseCase {
        return FetchPokemonUseCaseImpl(pokemonRepository)
    }

    @Provides
    fun provideGetScoreUseCase(keyValueStorage: KeyValueStorage): GetScoreUseCase {
        return GetScoreUseCaseImpl(keyValueStorage)
    }


    @Provides
    fun providePokemonAPI(): PokemonAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(PokemonAPI::class.java)
    }
}
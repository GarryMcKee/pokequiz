package com.gmk0232.whosthatpokemon.common.di

import android.content.Context
import androidx.room.Room
import com.gmk0232.whosthatpokemon.common.data.PokeQuizDatabase
import com.gmk0232.whosthatpokemon.feature.quiz.data.local.KeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providePokeQuizDatabase(@ApplicationContext context: Context): PokeQuizDatabase {
        val db = Room.databaseBuilder(
            context,
            PokeQuizDatabase::class.java, "pokequizdatabase"
        ).build()

        return db
    }


    @Provides
    @Singleton
    fun provideKeyValueStorage(@ApplicationContext context: Context): KeyValueStorage {
        return KeyValueStorage(context)
    }

}
package com.gmk0232.whosthatpokemon.common.di

import com.gmk0232.whosthatpokemon.common.dispatcher.AppDispatcherProvider
import com.gmk0232.whosthatpokemon.common.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DispatcherModule {
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = AppDispatcherProvider()

}
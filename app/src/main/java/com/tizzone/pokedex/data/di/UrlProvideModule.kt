package com.tizzone.pokedex.data.di

import com.tizzone.pokedex.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UrlProvideModule {

    @Provides
    fun providesBaseUrl() = BASE_URL
}

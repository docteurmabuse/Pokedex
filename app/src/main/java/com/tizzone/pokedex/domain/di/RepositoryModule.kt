package com.tizzone.pokedex.domain.di

import com.tizzone.pokedex.data.network.PokemonsApi
import com.tizzone.pokedex.domain.model.mappers.PokemonDtoMapper
import com.tizzone.pokedex.domain.repositories.PokemonsRepository
import com.tizzone.pokedex.domain.repositories.PokemonsRepositoryImpl
import com.tizzone.pokedex.domain.usescases.GetAllPokemons
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun providesGetAllPokemons(
        repository: PokemonsRepository
    ): GetAllPokemons {
        return GetAllPokemons(
            pokemonsRepository = repository
        )
    }

    @Singleton
    @Provides
    fun providesPokemonsRepository(
        apiService: PokemonsApi,
        mapper: PokemonDtoMapper
    ): PokemonsRepository {
        return PokemonsRepositoryImpl(
            pokemonsApi = apiService,
            mapper = mapper
        )
    }

    @Singleton
    @Provides
    fun providesPokemonsDtoMapper(): PokemonDtoMapper {
        return PokemonDtoMapper()
    }
}

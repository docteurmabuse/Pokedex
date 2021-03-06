package com.tizzone.pokedex.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tizzone.pokedex.data.network.PokemonsApi
import com.tizzone.pokedex.domain.model.Pokemon
import com.tizzone.pokedex.domain.model.PokemonDetails
import com.tizzone.pokedex.domain.model.mappers.PokemonDetailDtoMapper
import com.tizzone.pokedex.domain.model.mappers.PokemonDtoMapper
import com.tizzone.pokedex.domain.pagingsource.PokemonsPagingSource
import com.tizzone.pokedex.domain.state.DataState
import com.tizzone.pokedex.utils.POKEMONS_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonsRepositoryImpl @Inject constructor(
    private val pokemonsApi: PokemonsApi,
    private val mapper: PokemonDtoMapper,
    private val mapperPokemonDetail: PokemonDetailDtoMapper
) : PokemonsRepository {
    override fun fetchPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = POKEMONS_PAGE_SIZE),
            pagingSourceFactory = { PokemonsPagingSource(pokemonsApi, mapper) }
        ).flow
    }

    override suspend fun fetchPokemonDetail(name: String): DataState<PokemonDetails> {
        val response = pokemonsApi.getPokemon(name)
        return when (response.isSuccessful) {
            true -> DataState.success(
                mapperPokemonDetail.mapDtoDomain(response.body()!!)
            )
            false -> DataState.error(response.errorBody().toString())
        }
    }
}

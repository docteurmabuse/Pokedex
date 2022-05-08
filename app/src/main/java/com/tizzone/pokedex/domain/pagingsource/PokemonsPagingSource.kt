package com.tizzone.pokedex.domain.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tizzone.pokedex.data.network.PokemonsApi
import com.tizzone.pokedex.domain.model.Pokemon
import com.tizzone.pokedex.domain.model.mappers.PokemonDtoMapper
import com.tizzone.pokedex.utils.POKEMONS_PAGE_SIZE
import com.tizzone.pokedex.utils.POKEMONS_STARTING_PAGE_INDEX
import okio.IOException
import retrofit2.HttpException

class PokemonsPagingSource(
    private val service: PokemonsApi,
    private val mapper: PokemonDtoMapper
) : PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val position = params.key ?: POKEMONS_STARTING_PAGE_INDEX
        return try {
            val response = service.getPokemons(offset = position, limit = params.loadSize)
            val pokemonList = response.results
            val nextKey = if (pokemonList.isEmpty()) {
                null
            } else {
                position + (params.loadSize / POKEMONS_PAGE_SIZE)
            }
            LoadResult.Page(
                data = mapper.toDomainPokemonList(pokemonList),
                prevKey = if (position == POKEMONS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}

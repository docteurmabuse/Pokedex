package com.tizzone.pokedex.domain.repositories

import androidx.paging.PagingData
import com.tizzone.pokedex.domain.model.Pokemon
import com.tizzone.pokedex.domain.model.PokemonDetails
import com.tizzone.pokedex.domain.state.DataState
import kotlinx.coroutines.flow.Flow

sealed interface PokemonsRepository {
    fun fetchPokemonList(): Flow<PagingData<Pokemon>>
    suspend fun fetchPokemonDetail(name: String): DataState<PokemonDetails>
}

package com.tizzone.pokedex.domain.repositories

import androidx.paging.PagingData
import com.tizzone.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonsRepository {
    fun fetchPokemonList(): Flow<PagingData<Pokemon>>
}

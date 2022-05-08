package com.tizzone.pokedex.domain.usescases

import com.tizzone.pokedex.domain.repositories.PokemonsRepository
import com.tizzone.pokedex.domain.state.DataState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPokemonDetails @Inject constructor(
    private val pokemonsRepository: PokemonsRepository,
) {
    operator fun invoke(name: String) = flow {
        try {
            emit(DataState.loading())
            emit(pokemonsRepository.fetchPokemonDetail(name))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown error"))
        }
    }
}

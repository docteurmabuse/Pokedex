package com.tizzone.pokedex.presentation.ui.pokemonList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tizzone.pokedex.domain.model.Pokemon
import com.tizzone.pokedex.domain.usescases.GetAllPokemons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getAllPokemons: GetAllPokemons
) : ViewModel() {
    fun fetchAllPokemons(): Flow<PagingData<Pokemon>> = getAllPokemons()
        .catch { e -> Timber.d("ERROR_LIST: $e") }
        .cachedIn(viewModelScope)
}

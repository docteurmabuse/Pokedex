package com.tizzone.pokedex.presentation.ui.pokemonDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tizzone.pokedex.domain.model.PokemonDetails
import com.tizzone.pokedex.domain.usescases.GetPokemonDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonsDetails: GetPokemonDetails
) : ViewModel() {
    private var _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private var _pokemonDetail = MutableLiveData<PokemonDetails?>()
    val pokemonDetail: LiveData<PokemonDetails?> get() = _pokemonDetail

    fun fetchPokemonDetails(name: String) {
        viewModelScope.launch {
            getPokemonsDetails(name.lowercase()).collect { dataState ->
                _loading.postValue(dataState.loading)
                dataState.data?.let {
                    _pokemonDetail.postValue(it)
                }
                dataState.error?.let { error ->
                    Timber.e("AppDebug: $error")
                }
            }
        }
    }
}

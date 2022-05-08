package com.tizzone.pokedex.domain.usescases

import com.tizzone.pokedex.domain.repositories.PokemonsRepository
import javax.inject.Inject

class GetAllPokemons @Inject constructor(
    private val pokemonsRepository: PokemonsRepository
) {
    operator fun invoke() = pokemonsRepository.fetchPokemonList()
}

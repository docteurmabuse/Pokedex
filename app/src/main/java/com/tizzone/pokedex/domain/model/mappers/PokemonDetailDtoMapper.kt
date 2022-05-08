package com.tizzone.pokedex.domain.model.mappers

import com.tizzone.pokedex.data.network.model.PokemonResponse
import com.tizzone.pokedex.domain.model.PokemonDetails
import javax.inject.Inject

class PokemonDetailDtoMapper @Inject
constructor() : ApiMapper<PokemonResponse, PokemonDetails> {
    override fun mapDtoDomain(itemDto: PokemonResponse): PokemonDetails {
        return PokemonDetails(
            id = itemDto.id!!,
            name = itemDto.name!!,
            typesItem = itemDto.types!!,
            imageUrl = getImageUrl(itemDto.id)
        )
    }
    private fun getImageUrl(id: Int) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

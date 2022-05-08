package com.tizzone.pokedex.domain.model.mappers

import com.tizzone.pokedex.data.network.model.ResultsItem
import com.tizzone.pokedex.domain.model.Pokemon
import javax.inject.Inject

class PokemonDtoMapper @Inject
constructor() : ApiMapper<ResultsItem, Pokemon> {
    override fun mapDtoDomain(itemDto: ResultsItem): Pokemon {
        return Pokemon(
            id = getId(itemDto.url),
            name = itemDto.name!!.replaceFirstChar { it.uppercase() },
            imageUrl = getImageUrl(itemDto.url)
        )
    }

    private fun getId(url: String?) =
        url?.substringAfterLast("pokemon/")?.filter { it.isDigit() }?.toIntOrNull() ?: 0

    private fun getImageUrl(url: String?) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getId(url)}.png"

    fun toDomainPokemonList(initial: List<ResultsItem>): List<Pokemon> {
        return initial.map { mapDtoDomain(it) }
    }
}

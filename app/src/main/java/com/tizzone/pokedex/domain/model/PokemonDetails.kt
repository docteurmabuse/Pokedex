package com.tizzone.pokedex.domain.model

import com.tizzone.pokedex.data.network.model.TypesItem

data class PokemonDetails(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val typesItem: List<TypesItem?>
)

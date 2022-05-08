package com.tizzone.pokedex.domain.model.mappers

interface ApiMapper<E, D> {
    fun mapDtoDomain(itemDto: E): D
}

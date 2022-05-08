package com.tizzone.pokedex.domain.model.mappers

import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class PokemonDtoMapperTest {
    var pokemonDtoMapper = spyk(PokemonDtoMapper())
    @Test
    fun testIfGetIdFromUrlIsWorking() {
        val method = pokemonDtoMapper.javaClass.getDeclaredMethod("getId", String::class.java)
        method.isAccessible = true
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = "https://pokeapi.co/api/v2/pokemon/12/"
        Assert.assertEquals(12, method.invoke(pokemonDtoMapper, *parameters))
    }

    @Test
    fun testIfGetIdFromEmptyUrlIsWorking() {
        val method = pokemonDtoMapper.javaClass.getDeclaredMethod("getId", String::class.java)
        method.isAccessible = true
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = ""
        Assert.assertEquals(0, method.invoke(pokemonDtoMapper, *parameters))
    }

    @Test
    fun testIfGetImageUrlIsWorking() {
        val expected = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/12.png"
        val method = pokemonDtoMapper.javaClass.getDeclaredMethod("getImageUrl", String::class.java)
        method.isAccessible = true
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = "https://pokeapi.co/api/v2/pokemon/12/"
        Assert.assertEquals(expected, method.invoke(pokemonDtoMapper, *parameters))
    }
}

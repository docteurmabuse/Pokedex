package com.tizzone.pokedex.data.network

import com.tizzone.pokedex.data.network.model.PokemonResponse
import com.tizzone.pokedex.data.network.model.PokemonsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonsApi {
    @GET("pokemon/")
    suspend fun getPokemons(
        @Query("limit")
        limit: Int = 20,
        @Query("offset")
        offset: Int = 0
    ): PokemonsResponse

    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name")
        name: String
    ): Response<PokemonResponse>
}

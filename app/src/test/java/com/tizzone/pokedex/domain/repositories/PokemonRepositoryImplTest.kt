package com.tizzone.pokedex.domain.repositories

import com.google.gson.GsonBuilder
import com.tizzone.pokedex.data.network.MockwebServerPokemonsResponse.pokemonsReponse
import com.tizzone.pokedex.data.network.PokemonsApi
import com.tizzone.pokedex.domain.model.Pokemon
import com.tizzone.pokedex.domain.model.mappers.PokemonDtoMapper
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class PokemonRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // System in test
    private lateinit var pokemonsApi: PokemonsApi

    // Dependencies
    private val dtoMapper = PokemonDtoMapper()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("")

        // Instantiate system in test
        pokemonsApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .create()
                )
            ).build()
            .create(PokemonsApi::class.java)
    }

    @Test
    fun testGetAllPokemonsFromNetworkIsWorking(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(pokemonsReponse)
        )
        var pokemons = listOf<Pokemon>()
        // Pokemon list should ne empty
        assert(pokemons.isEmpty())
        // Adding api result if pokemon in the list
        pokemons = pokemonsApi.getPokemons().let {
            dtoMapper.toDomainPokemonList(it.results)
        }
        // Pokemonlist should not be empty
        assert(pokemons.isNotEmpty())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

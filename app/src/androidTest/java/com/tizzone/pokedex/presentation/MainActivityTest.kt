package com.tizzone.pokedex.presentation

import androidx.lifecycle.lifecycleScope
import androidx.paging.ItemSnapshotList
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.tizzone.pokedex.R
import com.tizzone.pokedex.data.network.MockwebServerPokemonsResponse
import com.tizzone.pokedex.domain.model.Pokemon
import com.tizzone.pokedex.presentation.ui.pokemonList.PokemonRecyclerViewAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import javax.inject.Inject
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonRecyclerViewAdapter
    private var list: ItemSnapshotList<Pokemon>? = null
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var okHttp: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()
        // Init mockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        mockWebServer.dispatcher = object : okhttp3.mockwebserver.Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(MockwebServerPokemonsResponse.pokemonsReponse)
            }
        }
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                okHttp
            )
        )
    }

    @Test
    fun areGetAllPokemonsDataAreSubmitInAdapter() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        // List should be empty
        assert(list.isNullOrEmpty())
        // When use case data with 34 articles are submit to PagingDataAdapter
        activityScenario.onActivity { activity ->
            activity.lifecycleScope.launch {
                recyclerView = activity.findViewById(R.id.pokemon_list)
                adapter = recyclerView.adapter as PokemonRecyclerViewAdapter
                adapter.loadStateFlow.distinctUntilChangedBy {
                    it.refresh
                }.collect {
                    list = adapter.snapshot()
                }
            }
        }
        // List should not be empty
        assert(list?.size!! > 0)
        activityScenario.close()
    }
}

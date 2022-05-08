package com.tizzone.pokedex.presentation.ui.pokemonList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tizzone.pokedex.databinding.FragmentPokemonListBinding
import com.tizzone.pokedex.domain.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment(), PokemonRecyclerViewAdapter.Interaction {
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var pokemonsAdapter: PokemonRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView

    private val newJobs: Job? = null
    private var _binding: FragmentPokemonListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.pokemonList
        setupRecyclerView(recyclerView)
        progressBar = binding.progressBar
        loadingText = binding.waitText

        setObserver()
    }

    private fun setObserver() {
        newJobs?.cancel()
        lifecycleScope.launch {
            viewModel.fetchAllPokemons().collectLatest { pokemonList ->
                pokemonsAdapter.submitData(pokemonList)
            }
            pokemonsAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Loading }
                .collect { binding.pokemonList.scrollToPosition(0) }
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView
    ) {
        pokemonsAdapter = PokemonRecyclerViewAdapter(this@PokemonListFragment)
        recyclerView.apply {
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as GridLayoutManager).orientation
            )
            adapter = pokemonsAdapter.apply {
                addLoadStateListener { loadstate ->
                    progressBar.isVisible = loadstate.refresh is LoadState.Loading ||
                        loadstate.append is LoadState.Loading
                    loadingText.isVisible = loadstate.refresh is LoadState.Loading ||
                        loadstate.append is LoadState.Loading

                    val errorState = when {
                        loadstate.append is LoadState.Error -> loadstate.append as LoadState.Error
                        loadstate.prepend is LoadState.Error -> loadstate.prepend as LoadState.Error
                        loadstate.refresh is LoadState.Error -> loadstate.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(position: Int, pokemon: Pokemon) {
        var bundle = Bundle()
    }
}

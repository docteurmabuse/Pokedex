package com.tizzone.pokedex.presentation.ui.pokemonDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tizzone.pokedex.R
import com.tizzone.pokedex.databinding.FragmentPokemonDetailBinding
import com.tizzone.pokedex.utils.ARG_POKEMON_NAME
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PokemonDetailFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */

    private lateinit var itemName: TextView
    private lateinit var image: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var detailToolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TypesAdapter

    private var toolbarLayout: CollapsingToolbarLayout? = null
    private var _binding: FragmentPokemonDetailBinding? = null
    private val viewModel: PokemonDetailsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            if (bundle.containsKey(ARG_POKEMON_NAME)) {
                bundle.getString(ARG_POKEMON_NAME)?.let { it ->
                    viewModel.fetchPokemonDetails(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemName = binding.itemName
        image = binding.imageDetail
        progress = binding.progress
        detailToolbar = binding.detailToolbar

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set back button and action bar
        (activity as AppCompatActivity).setSupportActionBar(detailToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = binding.typeRecyclerView
        setupRecyclerView(recyclerView)

        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemonDetails ->
            toolbarLayout?.title = pokemonDetails?.name
            adapter.submitList(pokemonDetails?.typesItem)
            image.load(pokemonDetails?.imageUrl) {
                allowHardware(false)
                listener(
                    onStart = {
                        progress.isVisible = true
                    },
                    onSuccess = { _, result ->
                        progress.isVisible = false
                        // Create the palette on a background thread.
                        Palette.Builder(result.drawable.toBitmap()).generate { palette ->
                            palette?.let {
                                val backgroundColor = palette.getDominantColor((ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary)))
                                binding.toolbarLayout.setBackgroundColor(backgroundColor)
                            }
                        }
                    }
                )
                crossfade(200)
                error(R.drawable.ic_baseline_catching_pokemon_24)
            }
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView
    ) {
        adapter = TypesAdapter()
        recyclerView.apply {
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as GridLayoutManager).orientation
            )
        }
        recyclerView.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

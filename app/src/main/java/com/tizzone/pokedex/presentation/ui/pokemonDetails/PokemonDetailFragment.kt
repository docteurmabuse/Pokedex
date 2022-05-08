package com.tizzone.pokedex.presentation.ui.pokemonDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tizzone.pokedex.R
import com.tizzone.pokedex.databinding.FragmentPokemonDetailBinding
import com.tizzone.pokedex.utils.ARG_POKEMON_NAME
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PokemonListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */

    lateinit var itemName: TextView
    lateinit var image: ImageView

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
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemonDetails ->
            itemName.text = pokemonDetails?.name
            image.load(pokemonDetails?.imageUrl) {
                allowHardware(false)
                listener(
                    onSuccess = { _, result ->
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
                transformations(CircleCropTransformation())
                transformations(RoundedCornersTransformation(25f))
                placeholder(R.drawable.ic_baseline_catching_pokemon_24)
                fallback(R.drawable.ic_baseline_catching_pokemon_24)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

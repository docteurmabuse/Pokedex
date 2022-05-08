package com.tizzone.pokedex.presentation.ui.pokemonDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tizzone.pokedex.databinding.FragmentPokemonDetailBinding
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

    lateinit var itemDetailTextView: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null

    private var _binding: FragmentPokemonDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemDetailTextView = binding.itemDetail

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

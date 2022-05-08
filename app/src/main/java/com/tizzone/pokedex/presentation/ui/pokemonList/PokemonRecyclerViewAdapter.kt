package com.tizzone.pokedex.presentation.ui.pokemonList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.tizzone.pokedex.R
import com.tizzone.pokedex.databinding.PokemonListContentBinding
import com.tizzone.pokedex.domain.model.Pokemon

class PokemonRecyclerViewAdapter(
    private val interaction: Interaction,
) : PagingDataAdapter<Pokemon, PokemonRecyclerViewAdapter.PokemonViewHolder>(POKEMON_COMPARATOR) {
    companion object {
        private val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.itemView.apply {
            if (pokemon != null) {
                holder.bind(pokemon)
                holder.nameView.text = resources.getString(R.string.pokemon_id, pokemon.id.toString(), pokemon.name)
                holder.imageView.load(pokemon.imageUrl) {
                    allowHardware(false)
                    listener(
                        onSuccess = { _, result ->
                            // Create the palette on a background thread.
                            Palette.Builder(result.drawable.toBitmap()).generate { palette ->
                                palette?.let {
                                    val backgroundColor = palette.getDominantColor((ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary)))
                                    holder.carView.setCardBackgroundColor(backgroundColor)
                                    val textColor = if (holder.isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                                    holder.nameView.setTextColor(textColor)
                                }
                            }
                        }
                    )
                    crossfade(true)
                    crossfade(200)
                    transformations(CircleCropTransformation())
                    transformations(RoundedCornersTransformation(25f))
                    placeholder(R.drawable.ic_baseline_catching_pokemon_24)
                    fallback(R.drawable.ic_baseline_catching_pokemon_24)
                }
            }
        }
    }

    inner class PokemonViewHolder(binding: PokemonListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.nameText
        val imageView: ImageView = binding.image
        val carView: CardView = binding.card
        fun bind(pokemon: Pokemon) {
            itemView.setOnClickListener {
                interaction.onItemSelected(bindingAdapterPosition, pokemon)
            }
        }
        fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (
                0.299 * Color.red(color) +
                    0.587 * Color.green(color) +
                    0.114 * Color.blue(color)
                ) / 255
            return darkness >= 0.5
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, pokemon: Pokemon)
    }
}

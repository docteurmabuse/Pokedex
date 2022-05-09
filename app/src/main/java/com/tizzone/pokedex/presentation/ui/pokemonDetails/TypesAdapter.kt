package com.tizzone.pokedex.presentation.ui.pokemonDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tizzone.pokedex.data.network.model.TypesItem
import com.tizzone.pokedex.databinding.PokemonTypesItemBinding

class TypesAdapter() : ListAdapter<TypesItem, TypesAdapter.TypeViewHolder>(ITEM_COMPARATOR) {

    inner class TypeViewHolder(
        val binding: PokemonTypesItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = PokemonTypesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.typeText.text = item.type?.name?.uppercase()
        with(holder.binding.typeText.context) {
            val colorType = resources.getIdentifier(item.type?.name, "color", packageName)
            holder.binding.typeText.setBackgroundColor(ContextCompat.getColor(this, colorType))
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<TypesItem>() {
    override fun areItemsTheSame(oldItem: TypesItem, newItem: TypesItem): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: TypesItem, newItem: TypesItem): Boolean {
        return oldItem == newItem
    }
}

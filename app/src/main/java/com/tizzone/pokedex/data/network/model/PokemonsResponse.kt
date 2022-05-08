package com.tizzone.pokedex.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonsResponse(

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("results")
    val results: List<ResultsItem> = listOf()
) : Parcelable

@Parcelize
data class ResultsItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
) : Parcelable

package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    @Json(name = "results")
    val results: List<PokemonResponse>
)

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)


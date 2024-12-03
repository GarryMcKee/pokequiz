package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailResponse(
    @Json(name = "name")
    val name: String,
    val sprites: Sprites
)

@JsonClass(generateAdapter = true)
data class Sprites(
    @Json(name = "other")
    val other: Other
)

@JsonClass(generateAdapter = true)
data class Other(
    @Json(name = "official-artwork")
    val officialArtworkUrl: OfficialArtwork
)

@JsonClass(generateAdapter = true)
data class OfficialArtwork(
    @Json(name = "front_default")
    val frontDefaultUrl: String
)
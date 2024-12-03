package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

const val FORECAST_ENDPOINT = "v2/pokemon?limit=150&offset=0"

interface PokemonAPI {
    @GET(FORECAST_ENDPOINT)
    suspend fun fetchPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>

    @GET
    suspend fun fetchPokemonDetail(@Url url: String): Response<PokemonDetailResponse>
}
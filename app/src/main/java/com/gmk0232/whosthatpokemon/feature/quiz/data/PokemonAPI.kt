package com.gmk0232.whosthatpokemon.feature.quiz.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val FORECAST_ENDPOINT = "v2/pokemon?limit=150&offset=0"

interface PokemonAPI {
    @GET(FORECAST_ENDPOINT)
    suspend fun fetchPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>
}
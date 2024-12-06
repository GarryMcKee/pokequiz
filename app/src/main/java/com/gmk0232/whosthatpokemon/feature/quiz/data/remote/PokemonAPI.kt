package com.gmk0232.whosthatpokemon.feature.quiz.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

const val FETCH_POKEMON_ENDPOINT = "v2/pokemon"

interface PokemonAPI {
    @GET(FETCH_POKEMON_ENDPOINT)
    suspend fun fetchPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>

    @GET
    suspend fun fetchPokemonDetail(@Url url: String): Response<PokemonDetailResponse>
}
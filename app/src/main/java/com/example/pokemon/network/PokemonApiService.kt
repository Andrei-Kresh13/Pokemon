package com.example.pokemon.network

import com.example.pokemon.model.PokemonListResponse
import com.example.pokemon.model.PokemonDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetails
}

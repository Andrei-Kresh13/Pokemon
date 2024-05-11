package com.example.pokemon

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailsActivity : AppCompatActivity() {
    private lateinit var service: PokemonApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)

        service = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiService::class.java)

        val pokemonName = intent.getStringExtra("pokemonName")
        loadPokemonDetails(pokemonName)
    }

    private fun loadPokemonDetails(pokemonName: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val pokemonDetails = service.getPokemonDetails(pokemonName!!)
                displayPokemonDetails(pokemonDetails)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayPokemonDetails(pokemonDetails: PokemonDetails) {
        val pokemonNameTextView = findViewById<TextView>(R.id.pokemonNameTextView)
        val pokemonHeightTextView = findViewById<TextView>(R.id.pokemonHeightTextView)
        val pokemonWeightTextView = findViewById<TextView>(R.id.pokemonWeightTextView)
        val pokemonImageView = findViewById<ImageView>(R.id.pokemonImageView)

        pokemonNameTextView.text = pokemonDetails.name
        pokemonHeightTextView.text = "Height: ${pokemonDetails.height}"
        pokemonWeightTextView.text = "Weight: ${pokemonDetails.weight}"

        Glide.with(this)
            .load(pokemonDetails.sprites.front_default)
            .placeholder(R.drawable.ic_pokemon_placeholder)
            .into(pokemonImageView)
    }
}

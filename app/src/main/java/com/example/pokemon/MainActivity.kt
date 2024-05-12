package com.example.pokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon.model.PokemonListItem
import com.example.pokemon.network.PokemonApiService
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonRecyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация RecyclerView
        pokemonRecyclerView = findViewById(R.id.pokemonRecyclerView)
        pokemonAdapter = PokemonAdapter()

        // Устанавливаем адаптер для RecyclerView
        pokemonRecyclerView.adapter = pokemonAdapter

        // Устанавливаем LayoutManager для RecyclerView
        pokemonRecyclerView.layoutManager = LinearLayoutManager(this)

        // Выполняем загрузку данных с помощью Retrofit и Coroutines
        loadData()
    }

    private fun loadData() {
        // Создаем экземпляр Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Создаем сервис для работы с API
        val service = retrofit.create(PokemonApiService::class.java)

        // Запускаем корутину для загрузки списка покемонов
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val pokemonList = service.getPokemonList().results
                pokemonAdapter.submitList(pokemonList)
                loadPokemonImages(pokemonList) // Загрузка изображений
            } catch (e: Exception) {
                // Обработка ошибки загрузки данных
                e.printStackTrace()
            }
        }
    }
    fun loadPokemonImages(pokemonList: List<PokemonListItem>) {
        for (pokemon in pokemonList) {
            val imageUrl = getPokemonImageUrl(pokemon.url)
            Glide.with(this)
                .load(imageUrl)
                .preload() // Предварительная загрузка изображений
        }
    }
    private fun getPokemonImageUrl(pokemonUrl: String): String {
        // Пример базового URL API для изображений покемонов в API PokeAPI
        val baseUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{pokemonId}.png\n"

        // Извлечение номера покемона из URL
        val pokemonId = pokemonUrl.substringAfterLast("/").dropLast(1)

        // Возвращение полного URL изображения покемона
        return "$baseUrl$pokemonId.png"
    }

}


package com.example.pokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
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
            } catch (e: Exception) {
                // Обработка ошибки загрузки данных
                e.printStackTrace()
            }
        }
    }
}


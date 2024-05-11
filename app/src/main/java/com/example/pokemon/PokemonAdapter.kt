package com.example.pokemon
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import kotlinx.android.synthetic.main.item_pokemon.view.*

class PokemonAdapter :
    ListAdapter<PokemonListItem, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon)
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonNameTextView = itemView.findViewById<TextView>(R.id.pokemonNameTextView)
        private val pokemonImageView = itemView.findViewById<ImageView>(R.id.pokemonImageView)

        fun bind(pokemon: PokemonListItem) {
            pokemonNameTextView.text = pokemon.name
            Glide.with(itemView)
                .load(getPokemonImageUrl(pokemon.url))
                .into(pokemonImageView)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PokemonDetailsActivity::class.java)
                intent.putExtra("pokemonName", pokemon.name)
                itemView.context.startActivity(intent)
                // Handle click event
            }
        }
    }

    private fun getPokemonImageUrl(url: String): String {
        // Extract Pokemon ID from URL
        val id = url.substringAfterLast("/").dropLast(1)
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }

    private class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonListItem>() {
        override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean {
            return oldItem == newItem
        }
    }
}

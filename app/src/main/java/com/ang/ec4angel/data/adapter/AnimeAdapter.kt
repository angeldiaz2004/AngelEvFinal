package com.ang.ec4angel.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ang.ec4angel.R
import com.ang.ec4angel.databinding.ItemsBinding
import com.ang.ec4angel.models.AnimeChan
import com.bumptech.glide.Glide

class AnimeAdapter : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {
    private var quotes = listOf<AnimeChan>()

    inner class ViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: AnimeChan) {
            binding.anime.text = "Anime: " + quote.anime
            binding.detalle.text = "Detalles: " + quote.quote
            binding.personaje.text = "Personajes: " + quote.character

            Glide.with(binding.root)
                .load(R.drawable.animechan)
                .into(binding.imagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.bind(quote)
    }

    override fun getItemCount() = quotes.size

    fun setQuotes(quotes: List<AnimeChan>) {
        this.quotes = quotes
        notifyDataSetChanged()
    }
}
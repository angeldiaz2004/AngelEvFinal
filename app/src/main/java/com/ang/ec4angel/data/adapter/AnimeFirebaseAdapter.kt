package com.ang.ec4angel.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ang.ec4angel.R
import com.ang.ec4angel.databinding.ItemAnimeBinding
import com.ang.ec4angel.models.Anime
import com.bumptech.glide.Glide

class AnimeFirebaseAdapter : RecyclerView.Adapter<AnimeFirebaseAdapter.AnimeViewHolder>() {

    private var animeList: List<Anime> = ArrayList()

    fun setData(data: List<Anime>) {
        animeList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime)
    }

    override fun getItemCount(): Int = animeList.size

    inner class AnimeViewHolder(private val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime) {
            binding.textTitulo.text = "Titulo: " + anime.titulo
            binding.textGenero.text = "Genero: " + anime.genero
            binding.textEpisodios.text = "Episodios: " + anime.episodios
            binding.textAnio.text = "Anio: " + anime.anio
            binding.textRating.text = "Rating: " + anime.rating

            // Cargar la imagen con Glide
            Glide.with(binding.root.context)
                .load(anime.foto) // URL de la imagen guardada en Firebase Storage
                .centerCrop()
                .into(binding.imageAnime)
        }
    }
}

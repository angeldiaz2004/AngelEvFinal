package com.ang.ec4angel.models

data class Anime(
    val animeId: String = "",
    val titulo: String = "",
    val genero: String = "",
    val episodios: Int = 0,
    val anio: Int = 0,
    val rating: Double = 0.0,
    val foto: String = ""
)
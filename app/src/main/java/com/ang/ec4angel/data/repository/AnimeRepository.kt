package com.ang.ec4angel.data.repository

import com.ang.ec4angel.data.service.AnimechanApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimeRepository {
    private val animechanApi = Retrofit.Builder()
        .baseUrl("https://animechan.xyz/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AnimechanApi::class.java)

    suspend fun getQuotesByAnime(title: String, page: Int) =
        animechanApi.getQuotesByAnime(title, page)

}
package com.ang.ec4angel.data.service

import com.ang.ec4angel.models.AnimeChan
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimechanApi {
    @GET("quotes/anime")
    suspend fun getQuotesByAnime(
        @Query("title") title: String,
        @Query("page") page: Int
    ): List<AnimeChan>
}
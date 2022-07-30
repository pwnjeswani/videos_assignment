package com.pawanjeswani.videosAssignment.network

import com.pawanjeswani.videosAssignment.model.VideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("videos/")
    suspend fun fetchWeather(
        @Query("key") app_id: String,
        @Query("q") query: String
    ): Response<VideosResponse>

}
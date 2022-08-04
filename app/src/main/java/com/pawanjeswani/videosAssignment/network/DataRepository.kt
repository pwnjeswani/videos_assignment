package com.pawanjeswani.videosAssignment.network

import com.pawanjeswani.videosAssignment.BuildConfig
import com.pawanjeswani.videosAssignment.model.VideosResponse
import retrofit2.Response
import javax.inject.Inject

class DataRepository @Inject constructor(private val dataService: APIService) : SafeApiCall {

    suspend fun fetchWeather(query: String, page:Int,pageSize:Int): Resource<Response<VideosResponse>> {
        val apiKey = BuildConfig.API_KEY
        return safeApi {
            dataService.fetchWeather(
                app_id = apiKey,
                query = query,
                page = page,
                pageSize= pageSize
            )
        }
    }

}

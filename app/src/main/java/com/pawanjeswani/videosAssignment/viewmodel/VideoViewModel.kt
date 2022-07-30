package com.pawanjeswani.videosAssignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawanjeswani.videosAssignment.model.VideosResponse
import com.pawanjeswani.videosAssignment.network.DataRepository
import com.pawanjeswani.videosAssignment.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _videosResponse: MutableLiveData<Resource<Response<VideosResponse>>> =
        MutableLiveData()

    val videosResponse: LiveData<Resource<Response<VideosResponse>>>
        get() = _videosResponse

    suspend fun fetchWeather(query: String) = viewModelScope.launch {
        _videosResponse.value = Resource.Loading
        _videosResponse.value = repository.fetchWeather(query)
    }
}

        
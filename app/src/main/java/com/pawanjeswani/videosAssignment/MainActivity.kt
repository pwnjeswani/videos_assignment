package com.pawanjeswani.videosAssignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pawanjeswani.videosAssignment.databinding.ActivityMainBinding
import com.pawanjeswani.videosAssignment.network.Resource
import com.pawanjeswani.videosAssignment.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    val videoViewModel: VideoViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startObserving()
        fetchVideosList()
    }

    private fun fetchVideosList() {
        lifecycleScope.launch {
            videoViewModel.fetchWeather("yellow flowers")
        }
    }

    private fun startObserving() {
        videoViewModel.videosResponse.observe(this) {
            when (it) {
                is Resource.Success -> {
                    val weatherResponse = it.value.body()
//                    showMainUi()
                    weatherResponse?.let { response ->
                        Toast.makeText(this, "Size is ${response.hits?.size}", Toast.LENGTH_SHORT).show()
                    }
                }
//                else -> handleLoaderAndError(it)
            }
        }
    }
}
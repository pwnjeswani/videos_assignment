package com.pawanjeswani.videosAssignment.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pawanjeswani.videosAssignment.databinding.ActivityMainBinding
import com.pawanjeswani.videosAssignment.model.Hit
import com.pawanjeswani.videosAssignment.network.Resource
import com.pawanjeswani.videosAssignment.view.adapter.StoryClickListener
import com.pawanjeswani.videosAssignment.view.adapter.VideoStoryAdapter
import com.pawanjeswani.videosAssignment.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), StoryClickListener {

    val videoViewModel: VideoViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        VideoStoryAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvVideos.adapter = adapter
        startObserving()
        fetchVideosList()

    }

    private fun fetchVideosList() {
        lifecycleScope.launch {
            videoViewModel.fetchWeather("red flowers")
        }
    }

    private fun startObserving() {
        videoViewModel.videosResponse.observe(this) {
            when (it) {
                is Resource.Success -> {
                    val videoResponse = it.value.body()
                    videoResponse?.let { response ->
                        Toast.makeText(this, "Size is ${response.hits?.size}", Toast.LENGTH_SHORT)
                            .show()
                        adapter.submitList(response.hits as MutableList<Hit>)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(this, "failed${it.errorBody}", Toast.LENGTH_SHORT).show()
                }

                Resource.Loading -> {

                }
            }
        }
    }

    override fun onStoryClicked(hit: Hit) {
        Toast.makeText(this, "clicked ${hit.pictureId}", Toast.LENGTH_SHORT).show()
    }
}
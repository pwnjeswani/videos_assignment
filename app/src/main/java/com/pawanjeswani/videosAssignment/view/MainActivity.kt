package com.pawanjeswani.videosAssignment.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.pawanjeswani.videosAssignment.R
import com.pawanjeswani.videosAssignment.databinding.ActivityMainBinding
import com.pawanjeswani.videosAssignment.model.Hit
import com.pawanjeswani.videosAssignment.network.Resource
import com.pawanjeswani.videosAssignment.view.adapter.StoryClickListener
import com.pawanjeswani.videosAssignment.view.adapter.VideoStoryAdapter
import com.pawanjeswani.videosAssignment.view.utils.setDrawable
import com.pawanjeswani.videosAssignment.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


const val STATE_RESUME_WINDOW = "resumeWindow"
const val STATE_RESUME_POSITION = "resumePosition"
const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
const val STATE_PLAYER_PLAYING = "playerOnPlay"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), StoryClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var controlView: PlayerControlView
    private lateinit var exoFullScreenIcon: ImageView
    private lateinit var exoMuteUnmuteIcon: ImageView
    private lateinit var mainFrameLayout: FrameLayout
    val mainHandler = Handler(Looper.getMainLooper())

    private var fullscreenDialog: Dialog? = null
    private var isMuted = false
    private var loadedNewVideo = false
    private var durationSet = false
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private var mediaItem: MediaItem? = null

    private val adapter by lazy {
        VideoStoryAdapter(this)
    }
    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fetch videos part
        binding.rvVideos.adapter = adapter
        startObserving()
        fetchVideosList()

        //video player setup
        playerView = binding.playerView
        controlView = playerView.findViewById(com.google.android.exoplayer2.ui.R.id.exo_controller)
        mainFrameLayout = binding.mainMediaFrame
        exoFullScreenIcon = playerView.findViewById(R.id.exo_fullscreen_icon)
        exoMuteUnmuteIcon = playerView.findViewById(R.id.exo_mute_unmute_icon)
        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)

        //full screen
        initFullScreenDialog()
        initFullScreenButton()

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
    }

    private fun startObserving() {
        videoViewModel.videosResponse.observe(this) {
            when (it) {
                is Resource.Success -> {
                    val videoResponse = it.value.body()
                    videoResponse?.let { response ->
                        val list = response.hits as MutableList<Hit>
                        onStoryClicked(list[0])
                        adapter.submitList(list)
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

    // api calling function
    private fun fetchVideosList() {
        lifecycleScope.launch {
            videoViewModel.fetchWeather("red flowers")
        }
    }

    private fun initPlayer() {
        releasePlayer()
        player = ExoPlayer
            .Builder(this)
            .build().apply {
                playWhenReady = isPlayerPlaying
                seekTo(0, 0)
                mediaItem?.let { setMediaItem(it, false) }
                prepare()
            }
        loadedNewVideo = false
        player.addAnalyticsListener(EventLogger())
        playerView.player = player
        if (isFullscreen) openFullscreenDialog()
        //Function to update position
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                playbackPosition = player.currentPosition;
                Log.d("handler", "$playbackPosition")
                mainHandler.postDelayed(this, 1000);
            }
        }, 1000)
    }

    private fun releasePlayer() {
        this::player.isInitialized.let {
            if (it) {
                if (loadedNewVideo) {
                    isPlayerPlaying = player.playWhenReady
                    playbackPosition = player.currentPosition
                    currentWindow = player.currentMediaItemIndex
                }
                player.release()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, player.currentMediaItemIndex)
        outState.putLong(STATE_RESUME_POSITION, player.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }


    // FULLSCREEN PART
    private fun initFullScreenDialog() {
        fullscreenDialog =
            object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    if (isFullscreen) closeFullscreenDialog()
                    super.onBackPressed()
                }
            }
    }

    private fun initFullScreenButton() {
        exoMuteUnmuteIcon.setDrawable(R.drawable.ic_baseline_volume_up_24)
        exoMuteUnmuteIcon.setOnClickListener { toggleMuteStatus() }
        exoFullScreenIcon.setOnClickListener {
            if (!isFullscreen) {
                openFullscreenDialog()
            } else {
                closeFullscreenDialog()
            }
        }
    }

    private fun toggleMuteStatus() {
        isMuted = !isMuted
        if (isMuted) {
            exoMuteUnmuteIcon.setDrawable(R.drawable.ic_baseline_volume_mute_24)
            player.volume = 0f
        } else {
            exoMuteUnmuteIcon.setDrawable(R.drawable.ic_baseline_volume_up_24)
            player.volume = 1.0f
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun openFullscreenDialog() {
        exoFullScreenIcon.setDrawable(R.drawable.full_screen_shrink)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        (playerView.parent as ViewGroup).removeView(playerView)
        fullscreenDialog?.addContentView(
            playerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        isFullscreen = true
        fullscreenDialog?.show()
    }

    private fun closeFullscreenDialog() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        (playerView.parent as ViewGroup).removeView(playerView)
        mainFrameLayout.addView(playerView)
        exoFullScreenIcon.setDrawable(R.drawable.ic_fullscreen_expand)

        isFullscreen = false
        fullscreenDialog?.dismiss()
    }

    override fun onBackPressed() {
        if (isFullscreen) {
            closeFullscreenDialog()
            return
        }
        super.onBackPressed()
    }

    override fun onStoryClicked(hit: Hit) {
        mediaItem = null
        mediaItem = hit.videos?.medium?.url?.let { MediaItem.fromUri(it) }
        loadedNewVideo = true
        initPlayer()
        hit.lastTimeClickedAt = System.currentTimeMillis()
    }
}
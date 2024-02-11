package com.droidblossom.archive.presentation.ui.capsule

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.droidblossom.archive.databinding.ActivityVideoBinding
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleActivity.Companion.CREATE_CAPSULE


class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding

    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePlayer()
    }


    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView = binding.playerView
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri((Uri.parse(intent.getStringExtra(VIDEO))))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    companion object {
        const val VIDEO = "video"

        fun newIntent(context: Context, videoUrl : String) =
            Intent(context, VideoActivity::class.java).apply {
                putExtra(CREATE_CAPSULE, videoUrl)
            }
    }
}
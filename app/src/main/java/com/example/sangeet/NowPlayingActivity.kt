package com.example.sangeet

import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NowPlayingActivity : AppCompatActivity() {

    private lateinit var playPauseButton: ImageView
    private lateinit var rewindButton: ImageView
    private lateinit var forwardButton: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var endTimeText: TextView

    private var isPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.now_playing)

        playPauseButton = findViewById(R.id.play_pause)
        rewindButton = findViewById(R.id.rewind)
        forwardButton = findViewById(R.id.forward)
        seekBar = findViewById(R.id.seekBar)
        currentTimeText = findViewById(R.id.current_time)
        endTimeText = findViewById(R.id.end_time)

        // Dummy static times
        currentTimeText.text = "2:34"
        endTimeText.text = "3:45"

        playPauseButton.setOnClickListener {
            isPlaying = !isPlaying
            if (isPlaying) {
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            }
        }

        rewindButton.setOnClickListener {
        }

        forwardButton.setOnClickListener {
        }

        seekBar.max = 100
        seekBar.progress = 60
    }
}

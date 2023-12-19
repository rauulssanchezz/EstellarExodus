package com.example.estellarexodus

import android.content.Context
import android.media.MediaPlayer

class MyMediaPlayer {
    companion object {
        var mediaPlayer: MediaPlayer? = null
        fun loadSong(context: Context, song: Int) {
            mediaPlayer = MediaPlayer.create(context, song)
        }

        fun startSong() {
            mediaPlayer!!.start()
        }

        fun stopSong() {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }

        fun pauseSong() {
            mediaPlayer!!.pause()
        }

        fun setLoop() {
            mediaPlayer!!.isLooping = true
        }
    }
}
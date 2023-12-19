package com.example.estellarexodus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button

class Start : AppCompatActivity() {
    val handler= Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button=findViewById<Button>(R.id.start)
        button.setOnClickListener {
            val newintent= Intent(this,Game::class.java)
            startActivity(newintent)
        }

        MyMediaPlayer.loadSong(this,R.raw.start_song)
        MyMediaPlayer.startSong()
        MyMediaPlayer.setLoop()
    }

    override fun onResume() {
        MyMediaPlayer.startSong()
        super.onResume()
    }

    override fun onPause() {
        MyMediaPlayer.pauseSong()
        super.onPause()
    }
    override fun onBackPressed() {
        onDestroy()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
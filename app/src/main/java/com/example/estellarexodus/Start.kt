package com.example.estellarexodus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.widget.Button

class Start : AppCompatActivity() {
    val handler= Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val shop=findViewById<Button>(R.id.shop)
        val start=findViewById<Button>(R.id.start)


        //BORRAR
        var sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        var coins=sharedPreferences.getInt("coins",0)
        coins=1000
        sharedPreferences.edit().apply {
            putInt("coins",coins)
            apply()
        }

        start.setOnClickListener {
            val newintent= Intent(this,Game::class.java)
            startActivity(newintent)
        }

        shop.setOnClickListener {
            val newIntent=Intent(this,Shop::class.java)
            startActivity(newIntent)
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
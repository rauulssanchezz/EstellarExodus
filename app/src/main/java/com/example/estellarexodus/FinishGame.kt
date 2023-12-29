package com.example.estellarexodus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView

class FinishGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_game)

        var textPoints=findViewById<TextView>(R.id.points)
        var textCoins=findViewById<TextView>(R.id.coins)
        var reload=findViewById<Button>(R.id.reload)
        var sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        var totalCoins=sharedPreferences.getInt("coins",0)
        totalCoins+=GameStateManager.coins
        sharedPreferences.edit().apply{
            putInt("coins",totalCoins)
            apply()
        }

        textPoints.text=textPoints.text.toString() + " "+GameStateManager.points.toString()
        textCoins.text=textCoins.text.toString()+" "+GameStateManager.coins.toString()

        reload.setOnClickListener {
            GameStateManager.firstTouch=false
            GameStateManager.running=true
            GameStateManager.points=0
            val newIntent= Intent(this,Game::class.java)
            startActivity(newIntent)
        }
    }

    override fun onBackPressed() {
        var newIntent=Intent(this,Start::class.java)
        GameStateManager.firstTouch=false
        GameStateManager.running=true
        GameStateManager.points=0
        startActivity(newIntent)
        super.onBackPressed()
    }

}
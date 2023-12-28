package com.example.estellarexodus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView

class FinishGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_game)

        var textPoints=findViewById<TextView>(R.id.points)
        var reload=findViewById<Button>(R.id.reload)

        textPoints.text=textPoints.text.toString() + " "+GameStateManager.points.toString()

        reload.setOnClickListener {
            GameStateManager.firstTouch=false
            GameStateManager.running=true
            val newIntent= Intent(this,Game::class.java)
            startActivity(newIntent)
        }
    }

    override fun onBackPressed() {
        var newIntent=Intent(this,Start::class.java)
        startActivity(newIntent)
        super.onBackPressed()
    }

}
package com.example.estellarexodus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView

class FinishGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_game)

        var reload=findViewById<Button>(R.id.reload)

        reload.setOnClickListener {
            val newIntent= Intent(this,Game::class.java)
            startActivity(newIntent)
        }
    }
}
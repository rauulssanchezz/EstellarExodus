package com.example.estellarexodus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.TextView

class Shop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        var sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        var coins=sharedPreferences.getInt("coins",0)

        var coinsText=findViewById<TextView>(R.id.coins)
        coinsText.text=coins.toString()

    }
}
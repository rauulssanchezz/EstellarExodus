package com.example.estellarexodus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView

class Shop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        var sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        var coins=sharedPreferences.getInt("coins",0)
        var totalShields=sharedPreferences.getInt("shields",0)
        var totalX2=sharedPreferences.getInt("x2",0)
        var totalMagnets=sharedPreferences.getInt("magnets",0)

        var shields=findViewById<TextView>(R.id.totalShields)
        var x2=findViewById<TextView>(R.id.totalX2)
        var magnets=findViewById<TextView>(R.id.totalMagnets)

        var buyShield=findViewById<Button>(R.id.buyShield)
        var buyX2=findViewById<Button>(R.id.buyx2)
        var buyMagnet=findViewById<Button>(R.id.buyMagnet)
        var priceShield=findViewById<TextView>(R.id.priceShield)
        var priceX2=findViewById<TextView>(R.id.pricex2)
        var priceMagnet=findViewById<TextView>(R.id.priceMagnet)

        var coinsText=findViewById<TextView>(R.id.coins)

        MyMediaPlayer.loadSong(this,R.raw.shop_theme1)
        MyMediaPlayer.startSong()
        MyMediaPlayer.setLoop()

        coinsText.text=coins.toString()

        shields.text=totalShields.toString()
        x2.text=totalX2.toString()
        magnets.text=totalMagnets.toString()

        buyShield.setOnClickListener {
            if (coins>=priceShield.text.toString().toInt()) {
                coins-=priceShield.text.toString().toInt()
                coinsText.text=coins.toString()
                totalShields++
                shields.text = totalShields.toString()
                sharedPreferences.edit().apply {
                    putInt("shields", totalShields)
                    putInt("coins",coins)
                    apply()
                }
            }
        }

        buyX2.setOnClickListener {
            if (coins>=priceX2.text.toString().toInt()) {
                coins-=priceX2.text.toString().toInt()
                coinsText.text=coins.toString()
                totalX2++
                x2.text = totalX2.toString()
                sharedPreferences.edit().apply {
                    putInt("x2", totalX2)
                    putInt("coins",coins)
                    apply()
                }
            }
        }

        buyMagnet.setOnClickListener {
            if (coins>=priceMagnet.text.toString().toInt()) {
                coins -= priceMagnet.text.toString().toInt()
                coinsText.text=coins.toString()
                totalMagnets++
                magnets.text = totalMagnets.toString()
                sharedPreferences.edit().apply {
                    putInt("magnets", totalMagnets)
                    putInt("coins",coins)
                    apply()
                }
            }
        }

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
        val newintent= Intent(this,Start::class.java)
        startActivity(newintent)
        super.onBackPressed()
    }
}
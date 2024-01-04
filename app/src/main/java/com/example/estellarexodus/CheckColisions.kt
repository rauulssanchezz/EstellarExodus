package com.example.estellarexodus

import android.content.Context
import android.graphics.Rect
import android.preference.PreferenceManager
import android.view.ViewGroup
import android.widget.ImageView

class CheckColisions {
    companion object {
        fun handleCollisionWithMeteorite(context:Context) {
            if (!GameStateManager.shield) {
                val ship = GameStateManager.ship
                ship!!.ship.setImageResource(R.drawable.explosion)
                GameStateManager.stopRunning()
            }else{
                var sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context)
                var shields=sharedPreferences.getInt("shields",0)
                shields--
                sharedPreferences.edit().apply {
                    putInt("shields",shields)
                    apply()
                }
                GameStateManager.shield=false
            }
        }

        fun checkCollisionWithMeteorite(meteoritePosition: Pair<Float, Float>): Boolean {
            val ship=GameStateManager.ship
            val shipBounds = Rect()
            ship!!.ship.getHitRect(shipBounds)

            val meteoriteBounds = Rect()
            meteoriteBounds.left = meteoritePosition.first.toInt()+110
            meteoriteBounds.top = meteoritePosition.second.toInt()
            meteoriteBounds.right = meteoritePosition.first.toInt()+25
            meteoriteBounds.bottom = meteoriteBounds.top + ship!!.height

            return Rect.intersects(shipBounds, meteoriteBounds)
        }
    }
}
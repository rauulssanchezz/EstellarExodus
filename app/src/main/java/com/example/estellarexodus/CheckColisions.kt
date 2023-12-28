package com.example.estellarexodus

import android.graphics.Rect

class CheckColisions {
    companion object {
        fun handleCollisionWithMeteorite() {
            val ship = GameStateManager.ship
            ship!!.ship.setImageResource(R.drawable.explosion)
            GameStateManager.stopRunning()
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
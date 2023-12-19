package com.example.estellarexodus

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.MotionEvent
import android.widget.ImageView

class Ship (val res: Resources,val ship: ImageView,var width: Int, var height: Int) {
    init {
        GameStateManager.ship = this
        val bitmap = BitmapFactory.decodeResource(res,R.drawable.ship1)
        ship.setImageBitmap(bitmap)
    }
    fun handleTouch(event: MotionEvent): Boolean {
        if (GameStateManager.running) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val centerX = ship.width / 2f
                    val centerY = ship.height / 2f
                    ship.x = event.rawX - centerX
                    ship.y = event.rawY - centerY

                }
            }
            return true
        }else{

        }
        return false
    }
}
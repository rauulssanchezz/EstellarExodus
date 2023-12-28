package com.example.estellarexodus

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class CoinsAndPoints(context: Context) {
    private val coin = ImageView(context)
    private val star = ImageView(context)
    private var context=context
    private var handler=Handler()


    init {
        coin.setImageResource(R.drawable.coin)
        star.setImageResource(R.drawable.star)

        coin.layoutParams = ViewGroup.LayoutParams(50, 50)
        star.layoutParams = ViewGroup.LayoutParams(70, 70)
    }

    fun setCoin(mainLayout: ViewGroup) {
        val coinClone = cloneImageView(coin)
        coinClone.x = Random.nextFloat() * (mainLayout.width - 50)
        coinClone.y = Random.nextFloat() * (mainLayout.height - 50)

        (context as? Activity)?.runOnUiThread {
            mainLayout.addView(coinClone, 0)
        }

        startAnimationLoop(coinClone, mainLayout)

        startCollisionChecking(coinClone, mainLayout,false)

        GlobalScope.launch {
            delay(20000)
            (context as? Activity)?.runOnUiThread {
                mainLayout.removeView(coinClone)
            }
        }
    }

    fun setStar(mainLayout: ViewGroup) {
        val starClone = cloneImageView(star)
        starClone.x = Random.nextFloat() * (mainLayout.width - 50)
        starClone.y = Random.nextFloat() * (mainLayout.height - 50)

        (context as? Activity)?.runOnUiThread {
            mainLayout.addView(starClone, 0)
        }

        startAnimationLoop(starClone, mainLayout)

        startCollisionChecking(starClone, mainLayout,true)

        GlobalScope.launch {
            delay(20000)
            (context as? Activity)?.runOnUiThread {
                mainLayout.removeView(starClone)
            }
        }
    }

    private fun startAnimationLoop(view: View, mainLayout: ViewGroup) {
        handler.post(animationLoop(view, mainLayout))
    }

    private fun cloneImageView(original: ImageView): ImageView {
        val clone = ImageView(context)
        clone.setImageDrawable(original.drawable)
        clone.layoutParams = ViewGroup.LayoutParams(50, 50)
        return clone
    }

    private fun animationLoop(view: View, mainLayout: ViewGroup): Runnable {
        val runnable = object : Runnable {
            override fun run() {
                if (view.parent != null) {
                    animateView(view)
                    handler.postDelayed(this, 600)
                }
            }
        }
        return runnable
    }

    private fun animateView(view: View) {
        view.animate().apply {
            scaleX(0.8f)
            scaleY(0.8f)
            duration = 300
        }.withEndAction {
            view.animate().apply {
                scaleX(1.1f)
                scaleY(1.1f)
                duration = 300
            }
        }
    }

    private fun startCollisionChecking(view: View, mainLayout: ViewGroup,star:Boolean) {
        handler.post(collisionChecking(view, mainLayout,star))
    }

    private fun collisionChecking(view: View, mainLayout: ViewGroup,star: Boolean): Runnable {
        val runnable = object : Runnable {
            override fun run() {
                if (view.parent != null) {
                    if (checkCollisionWithShip(view)) {
                        // Manejar colisión con la nave
                        handleCollisionWithShip(mainLayout,view,star)
                    }
                    handler.postDelayed(this, 16) // Verificar colisión cada 16 milisegundos (aproximadamente 60 FPS)
                }
            }
        }
        return runnable
    }

    private fun checkCollisionWithShip(view: View): Boolean {
        val shipBounds = getShipBounds()
        val viewBounds = Rect()
        view.getHitRect(viewBounds)

        return Rect.intersects(shipBounds, viewBounds)
    }

    private fun getShipBounds(): Rect {
        val shipBounds = Rect()
        val ship = GameStateManager.ship?.ship
        ship?.getHitRect(shipBounds)
        return shipBounds
    }

    private fun handleCollisionWithShip(mainLayout: ViewGroup,view: View,star:Boolean) {
        mainLayout.removeView(view)
        if (star) {
            var newView = TextView(context)
            newView.text = "+100"
            GameStateManager.points += 100
            newView.x = view.x
            newView.y = view.y

            handler.post(animationPoints(newView))

            mainLayout.addView(newView)

            GlobalScope.launch {
                delay(1000)
                (context as? Activity)?.runOnUiThread {
                    mainLayout.removeView(newView)
                }
            }
        }
    }

    private fun animationPoints(newView:View):Runnable{
        val run = object :Runnable{
            override fun run() {
                newView.y=newView.y-1
                handler.postDelayed(this,8)
            }
        }
        return run
    }
}
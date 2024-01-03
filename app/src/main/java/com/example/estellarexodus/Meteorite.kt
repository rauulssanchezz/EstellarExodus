package com.example.estellarexodus

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.preference.PreferenceManager
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import kotlin.random.Random

class Meteorite (val res: Resources, val meteorite: ImageView, var width: Int, var heigh: Int) {
    var shieldCollisionHandled: Boolean = false
    fun getPositionDuringAnimation(animatedValue: Float, x: Float): Pair<Float, Float> {
        val x = x
        val y = meteorite.y + animatedValue
        return Pair(x, y)
    }

    fun launchMeteorite(mainLayout: ViewGroup, context: Context) {
        val meteoriteClone = cloneImageView(meteorite,context)
        // Set the initial position of the meteorite at the top and in a random position.
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.width = width
        layoutParams.height = heigh
        meteoriteClone.layoutParams = layoutParams
        meteoriteClone.x = Random.nextFloat() * (mainLayout.width - width)
        meteoriteClone.y = - heigh.toFloat()

        // Add the meteorite to the main layout.
        mainLayout.addView(meteoriteClone,0)
        meteoriteClone.bringToFront()

        // Animate the meteorite downward.
        val animation = ObjectAnimator.ofFloat(
            meteoriteClone,
            "translationY",
            mainLayout.height.toFloat()
        )
        animation.duration = 1000 // Adjust the duration according to your needs.
        animation.interpolator = LinearInterpolator()
        animation.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            var newPosition = this.getPositionDuringAnimation(animatedValue,meteoriteClone.x)

            if (!this.shieldCollisionHandled && CheckColisions.checkCollisionWithMeteorite(newPosition)) {
                mainLayout.removeView(meteoriteClone)
                CheckColisions.handleCollisionWithMeteorite(context, mainLayout, meteoriteClone)
                this.shieldCollisionHandled = true
            }
            }

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Remove the meteorite at the end of the animation.
                mainLayout.removeView(meteoriteClone)
            }
        })
        animation.start()
    }
    private fun cloneImageView(original: ImageView, context: Context): ImageView {
        val clone = ImageView(context)
        clone.setImageDrawable(original.drawable)
        // Copy any other properties that you need from the original ImageView
        return clone
    }
}
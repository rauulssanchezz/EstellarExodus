package com.example.estellarexodus

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class CoinsAndPoints(context: Context) {
    private val coin = ImageView(context)
    private val star = ImageView(context)
    private var context=context

    init {
        coin.setImageResource(R.drawable.coin)
        star.setImageResource(R.drawable.star)

        coin.layoutParams = ViewGroup.LayoutParams(50, 50)
        star.layoutParams = ViewGroup.LayoutParams(50, 50)
    }

     fun setCoin(mainLayout:ViewGroup) {
        val coinClone = cloneImageView(coin)
        coinClone.x = Random.nextFloat() * (mainLayout.width - 50)
        coinClone.y = Random.nextFloat() * (mainLayout.height - 50)
        mainLayout.addView(coinClone, 0)

         val parentView = coinClone.parent as? ViewGroup
         parentView?.removeView(coinClone)

         (context as? Activity)?.runOnUiThread {
             mainLayout.addView(coinClone, 0)
         }

        GlobalScope.launch {
            delay(20000)
            (context as? Activity)?.runOnUiThread {
                mainLayout.removeView(coinClone)
            }
        }
    }

     fun setStar(mainLayout:ViewGroup) {
        val starClone = cloneImageView(star)
        starClone.x = Random.nextFloat() * (mainLayout.width - 50)
        starClone.y = Random.nextFloat() * (mainLayout.height - 50)

         val parentView = starClone.parent as? ViewGroup
         parentView?.removeView(starClone)

         (context as? Activity)?.runOnUiThread {
             mainLayout.addView(starClone, 0)
         }

        GlobalScope.launch {
            delay(20000)
            (context as? Activity)?.runOnUiThread {
                mainLayout.removeView(starClone)
            }
        }
    }

    private fun cloneImageView(original: ImageView): ImageView {
        val clone = ImageView(context)
        clone.setImageDrawable(original.drawable)
        clone.layoutParams = ViewGroup.LayoutParams(50, 50)
        return clone
    }
}
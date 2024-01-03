package com.example.estellarexodus

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import java.util.Objects

class FinishGame : AppCompatActivity() {
    private var handler=Handler()
    private lateinit var textPoints:TextView
    private lateinit var view:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_game)

        textPoints=findViewById(R.id.textPoints)
        var textCoins=findViewById<TextView>(R.id.textCoins)
        view=findViewById(R.id.view)
        var reload=findViewById<Button>(R.id.btnRestartGame)
        var sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        var totalCoins=sharedPreferences.getInt("coins",0)
        var totalX2=sharedPreferences.getInt("x2",0)
        val applyBonus = shouldApplyBonus(totalX2)

        if (applyBonus) {
            totalX2--
            doublePoints()
            showBonus()
        }

        totalCoins+=GameStateManager.coins
        sharedPreferences.edit().apply{
            putInt("coins",totalCoins)
            putInt("x2",totalX2)
            apply()
        }

        textPoints.text=textPoints.text.toString() + " "+GameStateManager.points.toString()
        textCoins.text=textCoins.text.toString()+" "+GameStateManager.coins.toString()

        reload.setOnClickListener {
            GameStateManager.firstTouch=false
            GameStateManager.running=true
            GameStateManager.points=0
            val newIntent= Intent(this,Game::class.java)
            startActivity(newIntent)
        }
    }


    private fun shouldApplyBonus(totalX2:Int): Boolean {
        // Lógica para determinar si el bonus aplica
        return totalX2>0
    }

    private fun doublePoints() {
        // Lógica para duplicar los puntos
        val currentPoints = GameStateManager.points
        val goal = currentPoints * 2
        var newPoints=currentPoints
        val run=object :Runnable{
            override fun run() {
                if (newPoints!=goal){
                    newPoints++
                    textPoints.text="Puntos: $newPoints"
                    view.setOnClickListener {
                        newPoints=goal
                    }
                    handler.postDelayed(this,1)
                }
            }
        }
        handler.post(run)
    }

    private fun showBonus() {
        // Lógica para mostrar el bonus
        val bonusTextView: TextView = findViewById(R.id.textBonus)
        bonusTextView.visibility=View.VISIBLE

        // Configurar animación de desvanecimiento y escala
        val fadeInOut = ObjectAnimator.ofFloat(bonusTextView, View.ALPHA, 0f, 1f)
        fadeInOut.duration = 1500 // Duración en milisegundos
        fadeInOut.interpolator = AccelerateDecelerateInterpolator()

        val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
            bonusTextView,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1.5f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1.5f)
        )
        scaleUp.duration = 1500 // Duración en milisegundos
        scaleUp.interpolator = LinearInterpolator()

        val scaleDown=ObjectAnimator.ofPropertyValuesHolder(
            bonusTextView,
            PropertyValuesHolder.ofFloat(View.SCALE_X,1.5f,1f,1.5f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y,1.5f,1f,1.5f)
        )

        scaleDown.duration = 1500 // Duración en milisegundos
        scaleDown.interpolator = LinearInterpolator()
        scaleDown.repeatCount = ValueAnimator.INFINITE // Repetir indefinidamente
        scaleDown.repeatMode = ValueAnimator.RESTART // Reiniciar en cada repetición

        // Combinar ambas animaciones
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeInOut, scaleUp,scaleDown)
        animatorSet.start()

    }

    override fun onBackPressed() {
        var newIntent=Intent(this,Start::class.java)
        GameStateManager.firstTouch=false
        GameStateManager.running=true
        GameStateManager.points=0
        startActivity(newIntent)
        super.onBackPressed()
    }

}
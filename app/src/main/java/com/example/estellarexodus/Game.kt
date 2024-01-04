package com.example.estellarexodus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class Game : AppCompatActivity() {
    private lateinit var shipImage: ImageView
    private lateinit var mainLayout: ViewGroup
    private var meteorites = mutableListOf<Meteorite>()
    private val handler = Handler()
    var time:Long=650
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var coinsAndStars:CoinsAndPoints
    private lateinit var pointsView: TextView
    private var coins=0
    private lateinit var coinsText:TextView
    lateinit var shieldImage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        var sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        var initialText=findViewById<TextView>(R.id.initialText)
        var touchImage=findViewById<ImageView>(R.id.touchImage)
        shieldImage=findViewById(R.id.shield)
        var shields=sharedPreferences.getInt("shields",0)
        var magnets=sharedPreferences.getInt("magnets",0)
        var magnetImage=findViewById<ImageView>(R.id.magnet)

        val context=this
        // Get an instance of the PowerManager.
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        // Acquire a WakeLock to keep the screen on.
        wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MiApp:WakeLock"
        )
        // Ensure that the screen stays on while the application is in the foreground.
        wakeLock?.acquire()

        if (magnets>0){
            GameStateManager.magnet=true
            magnetImage.visibility=ImageView.VISIBLE
        }

        if (shields>0){
            GameStateManager.shield=true
            shieldImage.visibility=ImageView.VISIBLE
        }else{
            GameStateManager.shield=false
            shieldImage.visibility=ImageView.INVISIBLE
        }

        coinsText=findViewById<TextView>(R.id.coinsText)

        //MainFrameView
        mainLayout = findViewById(R.id.mainLayout)

        val runnable=object :Runnable{
            override fun run() {
                initialText.animate().apply {
                    scaleX(0.9f)
                    scaleY(0.9f)
                    duration = 1000
                }.withEndAction {
                    initialText.animate().apply {
                        scaleX(1f)
                        scaleY(1f)
                        duration = 1000
                    }
                }
                touchImage.animate().apply {
                    scaleX(0.9f)
                    scaleY(0.9f)
                    duration = 1000
                }.withEndAction {
                    touchImage.animate().apply {
                        scaleX(1f)
                        scaleY(1f)
                        duration = 1000
                    }
                }
                if (!GameStateManager.firstTouch) {
                    handler.postDelayed(this, 2000)
                }
            }
        }

        mainLayout.setOnClickListener{
            mainLayout.removeView(initialText)
            mainLayout.removeView(touchImage)
            GameStateManager.firstTouch=true
        }

        handler.postDelayed(runnable,0)

        //Music
        MyMediaPlayer.loadSong(this, R.raw.gamemusic1)
        MyMediaPlayer.startSong()
        MyMediaPlayer.setLoop()

        //Ship
        shipImage = findViewById(R.id.ship)
        val ship = Ship(
            resources, shipImage, resources.getDimensionPixelSize(R.dimen.height)
        )
        GameStateManager.initializeShip(ship)

        ship.ship.setOnTouchListener { _, event ->
            ship.handleTouch(event)
        }

        //Meteorites
        initializeMeteorites(context)

        //Points and coins
        pointsView = findViewById(R.id.pointsView)
        coinsAndStars = CoinsAndPoints(context)

        val runnableMain=object :Runnable{
            var cont=0
            override fun run() {
                if (GameStateManager.firstTouch && cont==0) {
                    // Set up a timer to launch meteorites periodically.
                    handler.postDelayed(run(context), time)
                    handler.postDelayed(coinsAndStars(), 3500)
                    handler.postDelayed(updatePoints(), 0)
                    shipImage.focusable=View.FOCUSABLE
                    cont++
                }
                handler.postDelayed(this,8)
            }
        }

        handler.postDelayed(runnableMain,0)
    }
    fun updateTime(): Long {
        if (time>150) {
            time -= 5
        }
        return time
    }

    fun updatePoints():Runnable{
        val sumPoints=object :Runnable {
            override fun run() {
                if (GameStateManager.running){
                    GameStateManager.points += 1
                    coins=GameStateManager.coins
                    coinsText.text=coins.toString()
                    pointsView.text=GameStateManager.points.toString()
                    handler.postDelayed(this,1)
                }
            }
        }
        return sumPoints
    }

    fun initializeMeteorites(context: Context){
        val meteoriteImage1= ImageView(context)
        meteoriteImage1.setImageResource(R.drawable.meteorite1)
        val meteoriteImage2= ImageView(context)
        meteoriteImage2.setImageResource(R.drawable.meteorite2)
        val meteoriteImage3= ImageView(context)
        meteoriteImage3.setImageResource(R.drawable.meteorite3)
        val meteorite1=Meteorite(resources, meteoriteImage1,resources.getDimensionPixelSize(R.dimen.width),resources.getDimensionPixelSize(R.dimen.height))
        val meteorite2=Meteorite(resources,meteoriteImage2,resources.getDimensionPixelSize(R.dimen.width),resources.getDimensionPixelSize(R.dimen.height))
        val meteorite3=Meteorite(resources,meteoriteImage3,resources.getDimensionPixelSize(R.dimen.width),resources.getDimensionPixelSize(R.dimen.height))
        meteorites= mutableListOf(meteorite1,meteorite2,meteorite3)
    }
    fun run(context: Context):Runnable{
        val runnable=object : Runnable {
            override fun run() {
                if (GameStateManager.running) {
                    if (shieldImage.visibility==ImageView.VISIBLE&&!GameStateManager.shield){
                        shieldImage.visibility=ImageView.INVISIBLE
                    }
                    var pos = Random.nextInt(0, meteorites.size)
                    meteorites[pos].launchMeteorite(mainLayout, context)
                    updateTime()
                    handler.postDelayed(this, time)
                }else{
                    handler.removeCallbacks(this)
                    handlePlayerLoss()
                }
            }
        }
        return runnable
    }

    fun coinsAndStars():Runnable{
        val runnable=object :Runnable{
            override fun run() {
                var random=Random.nextInt(0,4)
                if (GameStateManager.running){
                    if (random==0) {
                        coinsAndStars.setCoin(mainLayout)
                    }else{
                        coinsAndStars.setStar(mainLayout)
                    }
                    handler.postDelayed(this,5000)
                }else{
                    handler.removeCallbacks(this)
                }
            }
        }
        return runnable
    }

    //Logic when the player loses.
    private fun handlePlayerLoss() {
        val newIntent = Intent(this, FinishGame::class.java)
        val run = object :Runnable {
            override fun run() {
                startActivity(newIntent)
            }
        }
        handler.postDelayed(run,200)
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
        GameStateManager.firstTouch=false
        GameStateManager.running=true
        GameStateManager.points=0
        startActivity(newintent)
        super.onBackPressed()
    }
}
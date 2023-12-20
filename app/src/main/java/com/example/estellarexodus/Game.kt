package com.example.estellarexodus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import kotlin.random.Random

class Game : AppCompatActivity() {
    private lateinit var shipImage: ImageView
    private lateinit var mainLayout: ViewGroup
    private var meteorites = mutableListOf<Meteorite>()
    private val handler = Handler()
    var time:Long=650
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var reload: Button
    private lateinit var finish: CardView
    private lateinit var coinsAndStars:CoinsAndPoints

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

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

        //MainFrameView
        mainLayout = findViewById(R.id.mainLayout)

        //Music
        MyMediaPlayer.loadSong(this, R.raw.gamemusic1)
        MyMediaPlayer.startSong()
        MyMediaPlayer.setLoop()

        //Ship
        shipImage = findViewById(R.id.ship)
        val ship=Ship(resources,shipImage,resources.getDimensionPixelSize(R.dimen.width),resources.getDimensionPixelSize(R.dimen.height))
        GameStateManager.initializeShip(ship)
        ship.ship.setOnTouchListener { _, event ->
            ship.handleTouch(event)
        }

        //Meteorites
        initializeMeteorites(context)

        coinsAndStars=CoinsAndPoints(context)

        // Set up a timer to launch meteorites periodically.
        handler.postDelayed(run(context), time)
        handler.postDelayed(coinsAndStars(),5000)

        finish=findViewById(R.id.finish)
        reload=findViewById(R.id.reload)

        reload.setOnClickListener {
            GameStateManager.running=true
            recreate()
        }
    }
    fun updateTime(): Long {
        if (time>150) {
            time -= 5
        }
        return time
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
                var random=Random.nextInt(0,2)
                if (GameStateManager.running){
                    if (random==0) {
                        coinsAndStars.setCoin(mainLayout)
                    }else{
                        coinsAndStars.setStar(mainLayout)
                    }
                    handler.postDelayed(this,15000)
                }else{
                    handler.removeCallbacks(this)
                }
            }
        }
        return runnable
    }

    //Logic when the player loses.
    private fun handlePlayerLoss() {
        finish.postDelayed({finish.visibility= View.VISIBLE},500)
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
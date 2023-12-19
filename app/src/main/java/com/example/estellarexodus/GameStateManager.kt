package com.example.estellarexodus

class GameStateManager {
    companion object {
        var ship: Ship? = null
        var running:Boolean=true
        fun initializeShip(ship: Ship) {
            this.ship = ship
        }

        fun stopRunning(){
            running=false
        }
    }
}
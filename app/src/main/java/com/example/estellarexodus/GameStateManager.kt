package com.example.estellarexodus

class GameStateManager {
    companion object {
        var ship: Ship? = null
        var running:Boolean=true
        var points:Int=0
        fun initializeShip(ship: Ship) {
            this.ship = ship
        }

        fun savePoints(points: Int){
            this.points=points
        }

        fun stopRunning(){
            running=false
        }
    }
}
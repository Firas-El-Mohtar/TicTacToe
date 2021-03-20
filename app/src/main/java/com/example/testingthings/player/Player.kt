package com.example.testingthings.player

import java.io.Serializable

data class Player(val playerIndex: Int, val playerSymbol: String, val playerName: String) : Serializable{
    var playerStepCount: Int = 0
}
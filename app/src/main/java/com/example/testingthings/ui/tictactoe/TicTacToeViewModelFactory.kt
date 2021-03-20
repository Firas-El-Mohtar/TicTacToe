package com.example.testingthings.ui.tictactoe

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.testingthings.player.Player
import androidx.lifecycle.ViewModelProvider
import com.example.testingthings.ingame.data.InGameDataSource
import com.example.testingthings.ingame.data.InGameRepository

class TicTacToeViewModelFactory(val player1: Player, val player2: Player, val prefs: SharedPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Player::class.java, Player::class.java, InGameRepository::class.java, SharedPreferences::class.java).newInstance(player1, player2, InGameRepository(
            InGameDataSource()
        ), prefs)
    }
}
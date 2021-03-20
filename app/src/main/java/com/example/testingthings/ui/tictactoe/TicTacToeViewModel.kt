package com.example.testingthings.ui.tictactoe

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingthings.ingame.data.InGameRepository
import com.example.testingthings.ingame.data.model.MatchHistory
import com.example.testingthings.login.data.Result
import com.example.testingthings.player.Player
class TicTacToeViewModel(val player1: Player, val player2: Player, val repo: InGameRepository, val prefs: SharedPreferences) : ViewModel() {

    private var initialState =  IntArray(9){0}
    private val totalTurns: Int
        get() = player1.playerStepCount + player2.playerStepCount
    val currentPlayer: Player
        get() = if (totalTurns % 2 == 0) player1 else player2
    private var myArray = initialState.copyOf(initialState.size)

    fun invalidate() {
        _stepsUpdated.value = myArray
        _playerOneTurn.value = player1
        _playerTwoTurn.value = player2
        checkGameOver()
    }

    fun buttonClicked(index: Int) {
        if (!isGameOver) {
            val player = currentPlayer
            player.playerStepCount++
            _totalTurnsChanged.value = totalTurns
            myArray[index] = player.playerIndex
            _stepsUpdated.value = myArray
            if (player.equals(player1)) {
                _playerOneTurn.value = player1
            } else {
                _playerTwoTurn.value = player2
            }
            checkGameOver()
        }
    }

    private val isGameOver: Boolean
        get() = playerOneWon(myArray) || playerTwoWon(myArray) || totalTurns == 9

    private fun checkGameOver() {
        if(playerOneWon(myArray)){
            _playerOneWon.value = Unit
        } else if(playerTwoWon(myArray)) {
            _playerTwoWon.value = Unit
        } else if(totalTurns == 9){
            _draw.value = Unit
        }
    }

    fun resetGame(){
        player1.playerStepCount = 0
        player2.playerStepCount = 0
        myArray = initialState.copyOf(initialState.size)
        _gameReset.value = Unit

    }

    fun playerSymbolForPlayerIndex(index: Int): String {
        return when (index) {
            player1.playerIndex -> player1.playerSymbol
            player2.playerIndex -> player2.playerSymbol
            else -> ""
        }
    }

    private var _stepsUpdated = MutableLiveData<IntArray>()
    val stepsUpdated: LiveData<IntArray>
        get() = _stepsUpdated

    private var _playerOneTurn = MutableLiveData<Player>()
    val playerOneTurn: LiveData<Player>
        get() = _playerOneTurn

    private var _playerTwoTurn = MutableLiveData<Player>()
    val playerTwoTurn: LiveData<Player>
        get() = _playerTwoTurn

    private var _playerOneWon = MutableLiveData<Unit>()
    val playerOneWon: LiveData<Unit>
        get() = _playerOneWon

    private var _playerTwoWon = MutableLiveData<Unit>()
    val playerTwoWon: LiveData<Unit>
        get() = _playerTwoWon

    private var _draw = MutableLiveData<Unit>()
    val draw: LiveData<Unit>
        get() = _draw

    private var _totalTurnsChanged = MutableLiveData<Int>()
    val totalTurnsChanged = _totalTurnsChanged

    private var _gameReset = MutableLiveData<Unit>()
    val gameReset: LiveData<Unit>
        get() = _gameReset

    private fun playerOneWon(array: IntArray):Boolean {
        if(((array[0] == 1)&&(array[1] == 1)&&(array[2] == 1))||
                ((array[3] == 1)&&(array[4] == 1)&&(array[5] == 1))||
                ((array[6] == 1)&&(array[7] == 1)&&(array[8] == 1))||
                ((array[0] == 1)&&(array[3] == 1)&&(array[6] == 1))||
                ((array[1] == 1)&&(array[4] == 1)&&(array[7] == 1))||
                ((array[2] == 1)&&(array[5] == 1)&&(array[8] == 1))||
                ((array[6] == 1)&&(array[4] == 1)&&(array[2] == 1))||
                ((array[8] == 1)&&(array[4] == 1)&&(array[0] == 1))) {
            return true
        }
        else{
            return false
        }
    }
    private fun playerTwoWon(array: IntArray):Boolean{
        if(((array[0] == 2)&&(array[1] == 2)&&(array[2] == 2))||
                ((array[3] == 2)&&(array[4] == 2)&&(array[5] == 2))||
                ((array[6] == 2)&&(array[7] == 2)&&(array[8] == 2))||
                ((array[0] == 2)&&(array[3] == 2)&&(array[6] == 2))||
                ((array[1] == 2)&&(array[4] == 2)&&(array[7] == 2))||
                ((array[2] == 2)&&(array[5] == 2)&&(array[8] == 2))||
                ((array[6] == 2)&&(array[4] == 2)&&(array[2] == 2))||
                ((array[8] == 2)&&(array[4] == 2)&&(array[0] == 2))) {
            return true
        }
        else{
            return false
        }
    }

    fun saveMatchHistory(timestamp: Long){
        val matchHistory = MatchHistory(player1.playerName, timestamp, playerOneWon(myArray))
        repo.saveMatchHistory(matchHistory){
            if (it is Result.Error){
            //TODO: let the user know there was an error
            }
        }
    }


}
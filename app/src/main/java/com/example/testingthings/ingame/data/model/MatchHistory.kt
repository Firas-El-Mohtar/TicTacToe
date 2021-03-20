package com.example.testingthings.ingame.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class MatchHistory(var playerName: String? = "", var duration: Long? = 0 ,var isWinner: Boolean? = false) {

    companion object {

        const val FIELD_PLAYER_NAME = "player"
        const val FIELD_IS_WINNER = "winner"
        const val FIELD_MATCH_DURATION = "duration"
    }
}
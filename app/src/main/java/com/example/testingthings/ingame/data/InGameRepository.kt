package com.example.testingthings.ingame.data

import com.example.testingthings.ingame.data.model.MatchHistory
import com.example.testingthings.login.data.Result

class InGameRepository(val inGameDataSource: InGameDataSource) {

    fun saveMatchHistory(matchHistory: MatchHistory, completionHandler: (Result<Boolean>)-> Unit){
        inGameDataSource.saveMatchHistory(matchHistory, completionHandler)

    }

}
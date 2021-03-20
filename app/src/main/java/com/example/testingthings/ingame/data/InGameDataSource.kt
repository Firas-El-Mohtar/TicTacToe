package com.example.testingthings.ingame.data

import com.example.testingthings.ingame.data.model.MatchHistory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.testingthings.login.data.Result

class InGameDataSource {
    val db = Firebase.firestore

    fun saveMatchHistory(matchHistory: MatchHistory, completionHandler: (Result<Boolean>)-> Unit){
        db.collection("matches").add(matchHistory).addOnSuccessListener {
            completionHandler(Result.Success(true))
        }
                .addOnFailureListener {
                    completionHandler(Result.Error(it))
                }
    }
}
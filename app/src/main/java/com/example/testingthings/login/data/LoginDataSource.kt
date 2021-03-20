package com.example.testingthings.login.data

import com.example.testingthings.login.data.Result
import com.example.testingthings.login.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.IOException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    // [START declare_auth]
    private var auth: FirebaseAuth = Firebase.auth
    // [END declare_auth]

    fun login(email: String, password: String, completionHandler: (Result<LoggedInUser>)-> Unit) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val user = LoggedInUser(auth.currentUser?.uid.toString(), auth.currentUser?.displayName.toString())
                        completionHandler(Result.Success(user))
                    } else {
                        // If sign in fails, send back the exception.
                        completionHandler(Result.Error(IOException("Error logging in", task.exception)))
                    }
                }
        // [END sign_in_with_email]
    }

    fun logout() {
        auth.signOut()
    }

    fun register(email: String, password: String, displayName: String, completionHandler: (Result<LoggedInUser>)-> Unit) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        auth.currentUser?.let {
                            update(it,displayName , completionHandler)
                        }

                    } else {
                        // If sign in fails, forward the error
                        completionHandler(Result.Error(IOException("Error signing up", task.exception)))
                    }
                }
        // [END create_user_with_email]
    }

    fun update(user: FirebaseUser, displayName: String, completionHandler: (Result<LoggedInUser>)-> Unit) {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = LoggedInUser(auth.currentUser?.uid.toString(), auth.currentUser?.displayName.toString())
                        completionHandler(Result.Success(user))
                    } else {
                        completionHandler(Result.Error(IOException("Error signing up", it.exception)))
                    }
                }
    }
}
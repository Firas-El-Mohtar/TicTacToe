package com.example.testingthings.login.data

import com.example.testingthings.login.data.model.LoggedInUser

/*
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(email: String, password: String, completionHandler: (Result<LoggedInUser>)->Unit){
        // handle login
        val result = dataSource.login(email, password) {
            if (it is Result.Success<LoggedInUser>) {
                setLoggedInUser(it.data)
            }
            completionHandler(it)
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    fun signup(email: String, password: String, displayName: String, completionHandler: (Result<LoggedInUser>)->Unit){
        // handle login
        val result = dataSource.register(email, password, displayName) {
            if (it is Result.Success<LoggedInUser>) {
                setLoggedInUser(it.data)
            }
            completionHandler(it)
        }
    }

}
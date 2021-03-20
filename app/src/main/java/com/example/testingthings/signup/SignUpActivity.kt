package com.example.testingthings.signup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testingthings.R
import com.example.testingthings.login.LoginActivity
import com.example.testingthings.login.LoginViewModel
import com.example.testingthings.login.LoginViewModelFactory
import com.example.testingthings.login.afterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity(){

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val displayName = findViewById<EditText>(R.id.displayname)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signup = findViewById<Button>(R.id.signup)
        val loading = findViewById<ProgressBar>(R.id.loading)

        viewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        viewModel.loginFormState.observe(this@SignUpActivity, Observer {
            val registerState = it ?: return@Observer

            // disable signup button unless  username / password / display name is valid
            signup.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
        })

        username.afterTextChanged{
            viewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    displayName.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString(),
                        displayName.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.signup(
                                username.text.toString(),
                                password.text.toString(),
                                displayName.text.toString()
                        )
                }
                false
            }

            signup.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.signup(username.text.toString(), password.text.toString(), displayName.text.toString())
            }
        }

    }


}
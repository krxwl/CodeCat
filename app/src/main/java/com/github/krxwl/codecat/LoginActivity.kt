package com.github.krxwl.codecat

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.krxwl.codecat.databinding.FragmentAuthenticationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//ключи к viewmodel
private const val KEY_EMAIL_TEXT = "emailText"
private const val KEY_PASSWORD_TEXT = "passwordText"

// ключи для контракта
private var LOGIN_RESULT_KEY = 0

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    // подключили viewmodel
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private lateinit var binding: FragmentAuthenticationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Login)
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            loginViewModel.passwordText = savedInstanceState.getString(KEY_PASSWORD_TEXT, "")
            loginViewModel.emailText = savedInstanceState.getString(KEY_EMAIL_TEXT, "")
        }

        auth = Firebase.auth

        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailTextInput.setFocusable(true)
        binding.passwordTextInput.setText(loginViewModel.passwordText)
        binding.emailTextInput.setText(loginViewModel.emailText)

        // запускает контракт с результатом активити
        val activityLauncher = registerForActivityResult(RegistrationActivityContract())
        { result ->
            // используем результат activity
            if (result) {
                finish()
            }
        }


        binding.loginButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()

            val snackbar = Snackbar.make(binding.fragmentAuth, "", Snackbar.LENGTH_SHORT)
            snackbar.view.layoutParams =
                (snackbar.view.layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.TOP
                }
            if (email.isBlank() || email.isEmpty()) {
                snackbar.setText(R.string.enter_an_email)
                snackbar.show()
            } else if (!email.contains('@')) {
                snackbar.setText(R.string.enter_the_correct_email)
                snackbar.show()
            } else if (password.isBlank() || password.isEmpty()) {
                snackbar.setText(R.string.enter_your_password)
                snackbar.show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        snackbar.setText("Successfully LoggedIn")
                    } else {
                        snackbar.setText("Log In failed")
                    }
                    snackbar.show()
                }
            }
        }

        binding.emailTextInput.setOnFocusChangeListener {  _, hasFocus ->
            Log.i(TAG, "СТРОКИ ПОЛУЧИЛ ${loginViewModel.passwordText}")
            if (!hasFocus) {
                loginViewModel.emailText = binding.emailTextInput.text.toString()
            }
        }

        binding.passwordTextInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                loginViewModel.passwordText = binding.passwordTextInput.text.toString()
            }
        }

        binding.registerButton.setOnClickListener {
            val emailPasswordArrayList = arrayListOf<String>(
                loginViewModel.passwordText, loginViewModel.emailText)
            activityLauncher.launch(emailPasswordArrayList)
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PASSWORD_TEXT, loginViewModel.passwordText)
        outState.putString(KEY_EMAIL_TEXT, loginViewModel.emailText)
    }
}

class LoginViewModel: ViewModel() {
    var emailText: String = ""
    var passwordText: String = ""
}



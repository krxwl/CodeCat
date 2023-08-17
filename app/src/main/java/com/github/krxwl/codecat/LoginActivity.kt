package com.github.krxwl.codecat

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.krxwl.codecat.databinding.FragmentAuthenticationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentAuthenticationBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Login)
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.fragment_authentication)

        binding.loginButton.setOnClickListener {
            val email = binding.emailTextInput.editText?.text.toString()
            val password = binding.passwordTextInput.editText?.text.toString()

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
    }


}
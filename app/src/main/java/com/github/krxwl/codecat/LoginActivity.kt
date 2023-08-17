package com.github.krxwl.codecat

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.krxwl.codecat.databinding.FragmentAuthenticationBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Login)
        super.onCreate(savedInstanceState)


        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.fragment_authentication)

        binding.loginButton.setOnClickListener {
            if (binding.emailTextInput.editText?.text?.isBlank() == true ||
                binding.emailTextInput.editText?.text?.isEmpty() == true) {

                val snackbar = Snackbar.make(binding.fragmentAuth, R.string.enter_an_email, Snackbar.LENGTH_SHORT)
                snackbar.view.layoutParams = (snackbar.view.layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.TOP
                }
                snackbar.show()

            }
        }

        fun Snackbar.gravityTop() {
            this.view.layoutParams = (this.view.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.TOP
            }
        }

    }



}
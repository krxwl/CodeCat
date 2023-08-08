package com.github.krxwl.codecat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_authentication)
        supportFragmentManager.beginTransaction().add(R.id.fragment_auth, AuthenticationFragment()).commit()
    }


}
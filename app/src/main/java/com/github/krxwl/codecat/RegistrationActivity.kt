package com.github.krxwl.codecat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.util.Arrays

private const val TAG = "RegistrationActivity"

class RegistrationActivity : AppCompatActivity(), FirstQuestionFragment.Callbacks,
    SecondQuestionFragment.Callbacks {

    private var enteredPassword: String = ""

    private val registrationViewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.registration_activity)
        setTheme(R.style.Theme_Login)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.registration_activity)

        if (currentFragment == null) {
            val newFragment = FirstQuestionFragment()
            val bundle = Bundle()
            bundle.putString("savedEmail", intent.getStringArrayExtra("email_password_key")?.get(1).toString())
            newFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .add(R.id.registration_activity, newFragment).commit()
        }

    }

    override fun passwordEntered(password: String) {
        enteredPassword = password
    }

    // на кнопку нажали поэтому меняем фрагмент
    override fun onNextStepButtonClicked() {
        val newFragment = SecondQuestionFragment()
        val bundle = Bundle()
        bundle.putString("savedPassword", intent.getStringArrayExtra("email_password_key")?.get(0).toString())
        bundle.putString("enteredPassword", enteredPassword)
        Log.i(TAG, "enteredPassword ${enteredPassword}")
        newFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.registration_activity, newFragment).addToBackStack(null).commit()
    }
}
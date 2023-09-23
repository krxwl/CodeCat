package com.github.krxwl.codecat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "RegistrationActivity"

class RegistrationActivity : AppCompatActivity(), FirstQuestionFragment.Callbacks {

    private val registrationViewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //registrationViewModel.email = intent.getStringArrayExtra("email_password_key")
        //Log.i(TAG, "email ${intent.getStringArrayExtra("email_password_key")}")
        setContentView(R.layout.registration_activity)
        setTheme(R.style.Theme_Login)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.registration_activity)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.registration_activity, FirstQuestionFragment()).commit()
        }

    }

    // на кнопку нажали поэтому меняем фрагмент
    override fun onNextStepButtonClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.registration_activity, SecondQuestionFragment()).addToBackStack(null).commit()
    }
}
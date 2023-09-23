package com.github.krxwl.codecat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class RegistrationActivity : AppCompatActivity(), FirstQuestionFragment.Callbacks {

    private val registrationViewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)
        setTheme(R.style.Theme_Login)
        supportFragmentManager.beginTransaction().add(R.id.registration_activity, FirstQuestionFragment()).commit()

    }

    // на кнопку нажали поэтому меняем фрагмент
    override fun onNextStepButtonClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.registration_activity, SecondQuestionFragment()).addToBackStack(null).commit()
    }
}
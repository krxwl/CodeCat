package com.github.krxwl.codecat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.krxwl.codecat.databinding.FragmentStepTwoBinding
import com.google.android.material.textfield.TextInputEditText

private const val KEY_EMAIL_TEXT = "emailText"
private const val KEY_PASSWORD_TEXT = "passwordText"
private const val TAG = "SecondQuestionFragment"
class SecondQuestionFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    private lateinit var binding: FragmentStepTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepTwoBinding.inflate(layoutInflater, container, false)

        if (savedInstanceState != null) {
            registrationViewModel.password = savedInstanceState.getString(KEY_PASSWORD_TEXT, "")
            Log.i(TAG, "password ${registrationViewModel.password}")

        }
        Log.i(TAG, "установила пароль ${registrationViewModel.password}")
        binding.passwordTextInputRegister.setText(registrationViewModel.password)

        binding.passwordTextInputRegister.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                registrationViewModel.password = binding.passwordTextInputRegister.text.toString()
            }
        }

        binding.registerButton.setOnClickListener {

        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PASSWORD_TEXT, registrationViewModel.password)
        outState.putString(KEY_EMAIL_TEXT, registrationViewModel.email)
    }
}
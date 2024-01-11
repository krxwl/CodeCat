package com.github.krxwl.codecat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.krxwl.codecat.activities.registrationactivity.RegistrationViewModel
import com.github.krxwl.codecat.databinding.FragmentStepTwoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val KEY_EMAIL_TEXT = "emailText"
private const val KEY_PASSWORD_TEXT = "passwordText"
private const val TAG = "SecondQuestionFragment"
class SecondQuestionFragment : Fragment() {

    interface Callbacks {
        fun passwordEntered(password: String)

        fun userRegistered()
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: Callbacks
    private lateinit var email: String

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

        auth = Firebase.auth

        email = arguments?.getString("email").toString()

        if (arguments?.getString("enteredPassword") != "") {
            binding.passwordTextInputRegister.setText(arguments?.getString("enteredPassword"))
        } else {
            binding.passwordTextInputRegister.setText(arguments?.getString("savedPassword"))
        }

        if (savedInstanceState != null) {
            registrationViewModel.password = savedInstanceState.getString(KEY_PASSWORD_TEXT, "")
        }


        binding.passwordTextInputRegister.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                registrationViewModel.password = binding.passwordTextInputRegister.text.toString()
                callbacks.passwordEntered(binding.passwordTextInputRegister.text.toString())
            }
        }

        binding.registerButton.setOnClickListener {
            val password = binding.passwordTextInputRegister.text.toString()
            val snackbar = Snackbar.make(binding.stepTwoFragment, "", Snackbar.LENGTH_SHORT)

            snackbar.view.layoutParams =
                (snackbar.view.layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.TOP
                }

            if (password.isBlank() || password.isEmpty()) {
                snackbar.setText(R.string.enter_your_password)
                snackbar.show()
            } else if (password.length < 8) {
                snackbar.setText(R.string.too_short_password)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email.strip(), password.strip()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        callbacks.userRegistered()

                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        snackbar.setText(R.string.authentication_failed)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PASSWORD_TEXT, registrationViewModel.password)
        outState.putString(KEY_EMAIL_TEXT, registrationViewModel.email)
    }
}
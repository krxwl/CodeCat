package com.github.krxwl.codecat.activities.registrationactivity

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
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.databinding.FragmentStepOneBinding
import com.google.android.material.snackbar.Snackbar

private const val KEY_EMAIL_TEXT = "emailText"
private const val KEY_PASSWORD_TEXT = "passwordText"
private const val TAG = "FirstQuestionFragment"
class FirstQuestionFragment : Fragment(R.layout.fragment_step_one) {

    private val registrationViewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    private lateinit var binding: FragmentStepOneBinding

    interface Callbacks {
        fun onNextStepButtonClicked()

        fun enteredEmail(email: String)
    }

    private var callbacks: Callbacks? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepOneBinding.inflate(layoutInflater, container, false)

        binding.emailTextinput.setText(arguments?.getString("savedEmail"))

        binding.nextStepButton.setOnClickListener {
            val email = binding.emailTextinput.text.toString()

            val snackbar = Snackbar.make(binding.stepOneFragment, "", Snackbar.LENGTH_SHORT)
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
            } else {
                callbacks?.enteredEmail(email)
                // говорим нашей активити через колбеки что на кнопку нажал
                callbacks?.onNextStepButtonClicked()
            }
        }

        binding.emailTextinput.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                registrationViewModel.email = binding.emailTextinput.text.toString()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "вернулись")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EMAIL_TEXT, registrationViewModel.email)
        outState.putString(KEY_PASSWORD_TEXT, registrationViewModel.password)
        Log.i(TAG, "iquwerouiwe")
    }
}
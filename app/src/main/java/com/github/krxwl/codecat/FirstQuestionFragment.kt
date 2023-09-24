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
import com.github.krxwl.codecat.databinding.FragmentStepOneBinding

//ключи к viewmodel
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
    }

    private var callbacks: Callbacks? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepOneBinding.inflate(layoutInflater, container, false)
        Log.i(TAG, "savedEmail ${arguments?.getString("savedEmail")}")
        binding.emailTextinput.setText(arguments?.getString("savedEmail"))

        // говорим нашей активити через колбеки что на кнопку нажали
        binding.nextStepButton.setOnClickListener {
            callbacks?.onNextStepButtonClicked()
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

    // вызывается когда фрагмент прикрепляется к activity
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
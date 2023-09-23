package com.github.krxwl.codecat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.krxwl.codecat.databinding.FragmentStepOneBinding

class FirstQuestionFragment : Fragment(R.layout.fragment_step_one) {

    lateinit var binding: FragmentStepOneBinding

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

        // говорим нашей активити через колбеки что на кнопку нажали
        binding.nextStepButton.setOnClickListener {
            callbacks?.onNextStepButtonClicked()
        }

        binding.emailTextinput.setOnFocusChangeListener { view, focus ->
            if (!hasFocus) {
                loginViewModel.emailText = binding.emailTextInput.text.toString()
            }
        }

        return binding.root
    }

    // вызывается когда фрагмент прикрепляется к activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
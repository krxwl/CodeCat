package com.github.krxwl.codecat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.krxwl.codecat.databinding.FragmentStepTwoBinding
import com.google.android.material.textfield.TextInputEditText

class SecondQuestionFragment : Fragment() {

    private lateinit var binding: FragmentStepTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}
package com.github.krxwl.codecat.activities.taskactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.databinding.TaskFragmentBinding
import com.github.krxwl.codecat.entities.Task
import com.google.android.material.transition.MaterialFadeThrough

class TaskFragment(val task: Task) : Fragment(R.layout.task_fragment) {

    lateinit var binding: TaskFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskFragmentBinding.inflate(layoutInflater)
        enterTransition = MaterialFadeThrough().apply {
            duration = 300L
        }
        exitTransition = MaterialFadeThrough().apply {
            duration = 300L
        }
        binding.taskNameTextview.text = task.taskName
        binding.taskText.text = task.task
        binding.inputData.text = task.input
        binding.outputData.text = task.output

        binding.codeView.setEnableHighlightCurrentLine(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
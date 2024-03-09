package com.github.krxwl.codecat.activities.taskactivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.databinding.TaskFragmentBinding
import com.github.krxwl.codecat.entities.Task
import com.github.krxwl.codecat.retrofit.AnswerData
import com.github.krxwl.codecat.retrofit.AnswerResult
import com.github.krxwl.codecat.retrofit.RetrofitInterface
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKRE9PRExFIiwic3ViIjoiV1MtQVBJLVRPS0VOIiwiY2xpZW50LWlkIjoiOGEzMTFiYTRjYzFiOGU3MGQxZmY1N2I1ZDQyYmZiYWMiLCJpYXQiOjE3MDU3NTY3NTgsImV4cCI6MTcwNTc1NjkzOH0.wjaz9_FEVeQbie1Lq3yX5zbIAIfq8l-vjtklPXi_sHY"
private const val TAG = "TaskFragment"

class TaskFragment(val task: Task) : Fragment(R.layout.task_fragment) {

    private lateinit var binding: TaskFragmentBinding
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl("https://api.jdoodle.com/")
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TaskFragmentBinding.inflate(layoutInflater)
        enterTransition = MaterialFadeThrough().apply {
            duration = 300L
        }
        binding.taskNameTextview.text = task.taskName
        binding.taskText.text = task.task
        binding.inputData.text = task.input
        binding.outputData.text = task.output

        binding.checkAnswerButton.setOnClickListener {
            val service = retrofit.create(RetrofitInterface::class.java)

            val answerData = AnswerData("8a311ba4cc1b8e70d1ff57b5d42bfbac",
                "b6a55384af2bf3344a282f9bb5d5a1cce6d72e3664df745d3bd941fb5956c533",
                binding.codeView.text.toString(),
                "",
                "python3",
                "0")

            CoroutineScope(Dispatchers.IO).launch {
                val call: AnswerResult = service.executeProgram(answerData)
                Log.i(TAG, "ПОЛУЧИЛ ${call}")
            }
        }

        binding.codeView.setEnableHighlightCurrentLine(true)
        return binding.root
    }
}
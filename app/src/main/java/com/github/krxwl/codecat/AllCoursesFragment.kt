package com.github.krxwl.codecat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.krxwl.codecat.databinding.AllCoursesFragmentBinding

class AllCoursesFragment() : Fragment(R.layout.all_courses_fragment) {

    interface Callbacks {
        fun onCourseSelected(id: Int)
    }

    private var callbacks: Callbacks? = null

    private lateinit var binding: AllCoursesFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllCoursesFragmentBinding.inflate(layoutInflater, container, false)

        binding.allCoursesRecyclerview.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

}
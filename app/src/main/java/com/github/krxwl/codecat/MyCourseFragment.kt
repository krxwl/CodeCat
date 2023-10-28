package com.github.krxwl.codecat

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.krxwl.codecat.customspinner.CustomAdapter
import com.github.krxwl.codecat.customspinner.CustomItem
import com.github.krxwl.codecat.databinding.MyCourseFragmentBinding

class MyCourseFragment : Fragment(R.layout.fragment_my_course) {

    lateinit var binding: MyCourseFragmentBinding
    lateinit var customArrayList: ArrayList<CustomItem>

    private val myCourseViewModel: MyCourseViewModel by lazy {
        ViewModelProvider(this)[MyCourseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyCourseFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myCourseViewModel.courseListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { courses ->
                courses?.let {
                    customArrayList = ArrayList()
                    for (course in courses) {
                        var (_, name, _, _, _, image) = course
                        customArrayList.add(CustomItem(name.toString(), image!!))
                    }
                    binding.customSpinner.adapter = CustomAdapter(this.requireContext(), customArrayList)
                }
            }
        )

    }
}

class MyCourseViewModel : ViewModel() {
    private val courseRepository = CourseRepository.get()
    val courseListLiveData = courseRepository.getCourses()
}
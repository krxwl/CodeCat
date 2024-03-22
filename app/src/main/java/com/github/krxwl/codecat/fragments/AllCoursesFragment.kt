package com.github.krxwl.codecat.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.Prefs
import com.github.krxwl.codecat.Prefs.Companion.dataStore
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.database.CourseRepository
import com.github.krxwl.codecat.databinding.AllCoursesFragmentBinding
import com.github.krxwl.codecat.entities.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "AllCoursesFragment"

class AllCoursesFragment : Fragment(R.layout.all_courses_fragment) {

    interface Callbacks {
        fun onCourseSelected(course: Course)
    }

    private var callbacks: Callbacks? = null

    private lateinit var binding: AllCoursesFragmentBinding
    private var adapter: CourseAdapter? = CourseAdapter(arrayListOf())

    private val allCoursesViewModel: AllCoursesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AllCoursesFragmentBinding.inflate(layoutInflater, container, false)
        binding.allCoursesRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.allCoursesRecyclerview.adapter = adapter
        return binding.root
    }

    private fun updateUI(courses: List<Course>) {
        adapter = CourseAdapter(ArrayList(courses))
        adapter!!.submitList(ArrayList(courses))
        binding.allCoursesRecyclerview.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allCoursesViewModel.courseListLiveData.observe(viewLifecycleOwner) { courses ->
            courses?.let {
                updateUI(courses)
                if (allCoursesViewModel.defaultCourse == -1) {
                    val mainSlide: Flow<Int> = requireContext().dataStore.data.map { preferences ->
                        preferences[Prefs.MAIN_COURSE_KEY] ?: 0
                    }
                    mainSlide.asLiveData().observe(viewLifecycleOwner) { slide ->
                        for ((i, c) in courses.withIndex()) {
                            if (c.id == slide) {
                                binding.allCoursesRecyclerview.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<ImageButton>(
                                    R.id.star_button
                                )
                                    ?.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_24))
                                allCoursesViewModel.defaultCourse = slide
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    inner class CourseAdapter(private var courses: ArrayList<Course>) : ListAdapter<Course, CourseAdapter.CourseHolder>(
        LinkDiffCallback()
    ) {
        override fun onBindViewHolder(holder: CourseHolder, position: Int) {
            val course = courses[position]
            holder.bind(course)
        }

        override fun getItemCount(): Int = courses.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CourseHolder {
            return CourseHolder(layoutInflater.inflate(R.layout.all_courses_item, parent, false))
        }

        inner class CourseHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

            private lateinit var course: Course

            private val nameTextView: TextView = itemView.findViewById(R.id.language_name)
            private val imageButton: ImageView = itemView.findViewById(R.id.logo_language)
            private val starButton: ImageButton = itemView.findViewById(R.id.star_button)

            init {
                itemView.setOnClickListener(this)
                starButton.setOnClickListener {
                    lifecycleScope.launch {
                        if (allCoursesViewModel.defaultCourse == course.id) {
                            requireContext().dataStore.edit { preferences ->
                                if (course.id != null) {
                                    preferences[Prefs.MAIN_COURSE_KEY] = 0
                                }
                            }
                            starButton.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_outline_24))
                        } else {
                            setPreviousCourseDisabled()
                            requireContext().dataStore.edit { preferences ->
                                if (course.id != null) {
                                    preferences[Prefs.MAIN_COURSE_KEY] = course.id!!
                                }
                            }
                            starButton.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_24))
                        }
                    }
                }
            }

            private fun setPreviousCourseDisabled() {
                val mainSlide: Flow<Int> = requireContext().dataStore.data.map { preferences ->
                    preferences[Prefs.MAIN_COURSE_KEY] ?: 0
                }
                mainSlide.asLiveData().observe(viewLifecycleOwner) { slide ->
                    for ((i, c) in courses.withIndex()) {
                        if (c.id == slide) {
                            binding.allCoursesRecyclerview.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<ImageButton>(R.id.star_button)
                                ?.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_outline_24))
                            allCoursesViewModel.defaultCourse = c.id!!
                            break
                        }
                    }
                }
            }

            fun bind(course: Course) {
                this.course = course
                nameTextView.text = this.course.name
                imageButton.setImageBitmap(this.course.image)
                if (course.id == allCoursesViewModel.defaultCourse) {
                    starButton.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_24))
                }
            }

            override fun onClick(p0: View?) {
                callbacks?.onCourseSelected(this.course)
            }
        }
    }

    class LinkDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            val (_, name, description, progress, _) = oldItem
            val (_, name1, description1, progress1, _) = newItem

            return name == name1 && description == description1 && progress == progress1
        }

        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }
    }

}

class AllCoursesViewModel : ViewModel() {
    private val courseRepository = CourseRepository.get()
    val courseListLiveData = courseRepository.getCourses()
    var defaultCourse: Int = -1
}
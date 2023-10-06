package com.github.krxwl.codecat

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.databinding.AllCoursesFragmentBinding

class AllCoursesFragment() : Fragment(R.layout.all_courses_fragment) {
/*
    interface Callbacks {
        fun onCourseSelected(id: Int)
    }

    private var callbacks: Callbacks? = null*/

    private lateinit var binding: AllCoursesFragmentBinding
    private var adapter: CourseAdapter? = CourseAdapter(arrayListOf())

    private val allCoursesViewModel: allCoursesViewModel by lazy {
        ViewModelProvider(this)[allCoursesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllCoursesFragmentBinding.inflate(layoutInflater, container, false)

        binding.allCoursesRecyclerview.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    fun updateUI(courses: List<Course>) {
        adapter = CourseAdapter(ArrayList(courses))
        adapter!!.submitList(ArrayList(courses))
        binding.allCoursesRecyclerview.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allCoursesViewModel.courseListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { courses ->
                courses?.let {
                    updateUI(courses)
                }
            }
        )

    }
/*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }*/

    inner class CourseAdapter(var courses: ArrayList<Course>) : ListAdapter<Course, CourseAdapter.CourseHolder>(LinkDiffCallback()) {
        override fun onBindViewHolder(holder: CourseAdapter.CourseHolder, position: Int) {
            if (courses.size != 0) {
                val course = courses[position]
                holder.bind(course)
            }
        }

        override fun getItemCount(): Int = courses.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CourseAdapter.CourseHolder {
            TODO("Not yet implemented")
        }

        inner class CourseHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

            private lateinit var course: Course

            private val nameTextView: TextView = itemView.findViewById(R.id.language_name)
            private val imageButton: ImageButton = itemView.findViewById(R.id.logo_language)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(course: Course) {
                this.course = course
                nameTextView.text = this.course.name
                imageButton.setImageBitmap(BitmapFactory.decodeByteArray(this.course.image, 0, this.course.image.size))
            }

            override fun onClick(p0: View?) {
                TODO("Not yet implemented")
            }
        }
    }

    class LinkDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            val (_, name, description, progress, main, _) = oldItem
            val (_, name1, description1, progress1, main1, _) = newItem

            return name == name1 && description == description1 && progress == progress1 && main == main1
        }

        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }
    }

}

class allCoursesViewModel : ViewModel() {
    private val courseRepository = CourseRepository.get()

    val courseListLiveData = courseRepository.getCourses()
}
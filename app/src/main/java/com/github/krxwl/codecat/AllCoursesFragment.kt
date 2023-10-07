package com.github.krxwl.codecat

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.databinding.AllCoursesFragmentBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

private val TAG = "AllCoursesFragment"

class AllCoursesFragment : Fragment(R.layout.all_courses_fragment) {
/*
    interface Callbacks {
        fun onCourseSelected(id: Int)
    }

    private var callbacks: Callbacks? = null*/

    private lateinit var binding: AllCoursesFragmentBinding
    private var adapter: CourseAdapter? = CourseAdapter(arrayListOf())

    private val allCoursesViewModel: AllCoursesViewModel by lazy {
        ViewModelProvider(this)[AllCoursesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllCoursesFragmentBinding.inflate(layoutInflater, container, false)

        binding.allCoursesRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.allCoursesRecyclerview.adapter = adapter
        return binding.root
    }

    private fun updateUI(courses: List<Course>) {
        Log.i(TAG, "отдал")
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
                    Log.i(TAG, "отдал из бд ${courses}")
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
            val course = courses[position]
            holder.bind(course)
        }

        override fun getItemCount(): Int = courses.size

        /*override fun submitList(list: MutableList<Course>?) {
            if (list == courses) {
                return
            }
            super.submitList(list)
        }*/

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CourseAdapter.CourseHolder {
            return CourseHolder(layoutInflater.inflate(R.layout.all_courses_item, parent, false))
        }

        inner class CourseHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

            private lateinit var course: Course

            private val nameTextView: TextView = itemView.findViewById(R.id.language_name)
            private val imageButton: ImageView = itemView.findViewById(R.id.logo_language)
            private val circularProgressBar: CircularProgressBar = itemView.findViewById(R.id.circularProgressBar)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(course: Course) {
                this.course = course
                nameTextView.text = this.course.name
                circularProgressBar.progress = this.course.progress?.toFloat()!!
                imageButton.setImageBitmap(this.course.image)
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

class AllCoursesViewModel : ViewModel() {
    private val courseRepository = CourseRepository.get()
    val courseListLiveData = courseRepository.getCourses()
}
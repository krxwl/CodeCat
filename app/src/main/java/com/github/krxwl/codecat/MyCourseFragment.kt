package com.github.krxwl.codecat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.customspinner.CustomAdapter
import com.github.krxwl.codecat.customspinner.CustomItem
import com.github.krxwl.codecat.database.CourseRepository
import com.github.krxwl.codecat.databinding.MyCourseFragmentBinding
import com.github.krxwl.codecat.entities.Submodule
import com.github.krxwl.codecat.entities.Task
import com.mikhaellopez.circularprogressbar.CircularProgressBar

private const val TAG = "MyCourseFragment"
class MyCourseFragment : Fragment(R.layout.my_course_fragment) {
    interface Callbacks {
        fun onSubmoduleSelected(id: Int)
    }

    private var callbacks: Callbacks? = null


    lateinit var binding: MyCourseFragmentBinding
    lateinit var customArrayList: ArrayList<CustomItem>
    private var adapter: SubmodulesAdapter? = SubmodulesAdapter(arrayListOf())

    private val myCourseViewModel: MyCourseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyCourseFragmentBinding.inflate(layoutInflater)


        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        binding.customSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                myCourseViewModel.setIndex(position)
            }
        }
        return binding.root
    }

    private fun updateUI(submodules: List<Submodule>) {
        val adapter = SubmodulesAdapter(ArrayList(submodules))
        adapter.submitList(ArrayList(submodules))
        binding.recyclerview.adapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myCourseViewModel.courseListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { courses ->
                courses?.let {
                    customArrayList = ArrayList()
                    for (course in courses) {
                        var (id, name, _, _, _, image) = course
                        customArrayList.add(CustomItem(id!!, name.toString(), image!!))
                    }
                    binding.customSpinner.adapter = CustomAdapter(this.requireContext(), customArrayList)
                    // TODO("СДЕЛАТЬ УСТАНОВКУ КУРСА ПО УМОЛЧАНИЮ")
                    val index = myCourseViewModel.getIndex()
                    Log.i(TAG, "ИНДЕКС ${index}")
                    if (index != null) {
                        myCourseViewModel.setIndex(index)
                        binding.customSpinner.setSelection(index)
                    }
                    //binding.customSpinner.setSelection(1)
                    //myCourseViewModel.setIndex(2)
                }
            }
        )

        myCourseViewModel.submodulesListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { submodules ->
                submodules?.let {
                    updateUI(submodules)
                }
            }
        )
    }

    inner class SubmodulesAdapter(var submodules: ArrayList<Submodule>) : ListAdapter<Submodule, SubmodulesAdapter.SubmoduleHolder>(LinkDiffCallback()) {
        override fun onBindViewHolder(holder: SubmodulesAdapter.SubmoduleHolder, position: Int) {
            val submodule = submodules[position]
            holder.bind(submodule)
        }

        override fun getItemCount(): Int = submodules.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SubmodulesAdapter.SubmoduleHolder {
            return SubmoduleHolder(layoutInflater.inflate(R.layout.my_course_item, parent, false))
        }

        inner class SubmoduleHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

            private lateinit var submodule: Submodule

            private val nameTextView: TextView = itemView.findViewById(R.id.language_name)
            private val imageView: ImageView = itemView.findViewById(R.id.logo_language)
            private val progressTextView: TextView = itemView.findViewById(R.id.textview_progress)
            private val circularProgressBar: CircularProgressBar = itemView.findViewById(R.id.circularProgressBar)

            init {
                itemView.setOnClickListener(this)
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            fun bind(submodule: Submodule) {
                this.submodule = submodule
                nameTextView.text = submodule.name
                circularProgressBar.progress = 0.toFloat()
                if (submodule.type == "task") {
                    imageView.setImageDrawable(resources.getDrawable(R.drawable.task))
                } else {
                    imageView.setImageDrawable(resources.getDrawable(R.drawable.document))
                }
                //imageButton.setImageBitmap(this.submodule.image)
                progressTextView.setText("${0}/15")
            }

            override fun onClick(p0: View?) {
                if (submodule.type.toString() == "task") {
                    myCourseViewModel.setSubmoduleId(submodule.id)
                    callbacks?.onSubmoduleSelected(submodule.id)
                }
            }
        }
    }

    class LinkDiffCallback : DiffUtil.ItemCallback<Submodule>() {
        override fun areContentsTheSame(oldItem: Submodule, newItem: Submodule): Boolean {
            val (_, progress, name, module, type) = oldItem
            val (_, progress2, name2, module2, type2) = newItem

            return progress == progress2 && name == name2 && module == module2 && type == type2
        }

        override fun areItemsTheSame(oldItem: Submodule, newItem: Submodule): Boolean {
            return oldItem.id == newItem.id
        }
    }

}

class MyCourseViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    val courseRepository = CourseRepository.get()
    val courseListLiveData = courseRepository.getCourses()
    var submodulesListLiveData: LiveData<List<Submodule>> = state.getLiveData<Int>("index").switchMap { id ->
        courseRepository.getSubmodules(id + 1)
    }
    var tasksListLiveData: LiveData<List<Task>> = state.getLiveData<Int>("id").switchMap { id ->
        courseRepository.getTasks(id)
    }
    //viewmodel с savedstatehandle-ом позволяет при каждом изменении переменной отправлять запрос к бд
    fun setIndex(index: Int) {
        state["index"] = index
    }

    fun getIndex(): Int? = state["index"]

    fun setSubmoduleId(id: Int) {
        state["id"] = id
    }
}
package com.github.krxwl.codecat.activities.taskactivity

import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.activities.mainactivity.MainActivity
import com.github.krxwl.codecat.database.CourseRepository
import com.github.krxwl.codecat.databinding.TaskActivityBinding
import com.github.krxwl.codecat.entities.Task
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "TaskActivity"

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: TaskActivityBinding
    private lateinit var adapter: TaskAdapter

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    var tasksArrayList: ArrayList<Task>? = null
    var submoduleId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TaskActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tasksArrayList = intent.extras?.getParcelableArrayList<Task>("tasks")
        submoduleId = tasksArrayList?.get(0)?.submodule!!
        taskViewModel.oldSavedTaskId = taskViewModel.courseRepository.getDefaultTaskId(tasksArrayList?.get(0)?.submodule!!)
        binding.taskRecyclerview.layoutManager = LinearLayoutManager(applicationContext)

        taskViewModel.oldSavedTaskId!!.observe(this) { id ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_place)

            if (id != null) {
                adapter = TaskAdapter(tasksArrayList!!, id)
                var currentTask: Task? = null
                for (task in tasksArrayList!!) {
                    if (task.id == id) {
                        currentTask = task
                    }
                }

                if (currentFragment == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_place, TaskFragment(currentTask!!)).commit()
                }
            } else {
                adapter = TaskAdapter(tasksArrayList!!, tasksArrayList!![0].id!!)

                if (currentFragment == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_place, TaskFragment(tasksArrayList!![0])).commit()
                }
            }

            binding.taskRecyclerview.adapter = adapter
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(TAG, "ЗАВЕРШАЮСЬ ")
            if (taskViewModel.newSavedTaskId != taskViewModel.oldSavedTaskId?.value) {
                lifecycleScope.launch(Dispatchers.IO) {
                    taskViewModel.courseRepository.commitSavedTaskId(
                        submoduleId!!,
                        taskViewModel.newSavedTaskId!!
                    )
                }
            }
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()

        }
        return super.onKeyDown(keyCode, event)
    }

    inner class TaskAdapter(var tasks: ArrayList<Task>, var currentTaskIndex: Int = 0) : ListAdapter<Task, TaskAdapter.TaskHolder>(LinkDiffCallback()) {

        lateinit var oldItem: TaskHolder

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        fun setTaskSelected(newView: TaskHolder) {
            if (oldItem.getTask().isSolved == "true") {
                oldItem.setTaskBlockBackground(R.drawable.task_solved_block)
            } else if (oldItem.getTask().isSolved == "false") {
                oldItem.setTaskBlockBackground(R.drawable.task_not_solved_block)
            }
            if (newView.getTask().isSolved == "true") {
                newView.setTaskBlockBackground(R.drawable.task_solved_selected)
            } else {
                newView.setTaskBlockBackground(R.drawable.task_not_solved_selected)
            }
            oldItem = newView
            taskViewModel.newSavedTaskId = newView.getTask().id
        }

        override fun getItemCount(): Int = tasks.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): TaskHolder {
            return TaskHolder(layoutInflater.inflate(R.layout.task_recyclerview_item, parent, false))
        }

        inner class TaskHolder(view: View?) : RecyclerView.ViewHolder(view!!),
            View.OnClickListener {

            private lateinit var task: Task

            private var taskBlock: View = itemView.findViewById(R.id.task_block)

            init {
                itemView.setOnClickListener(this)
            }

            fun getTask(): Task {
                return task
            }
            fun setTaskBlockBackground(background: Int) {
                taskBlock.background =
                    AppCompatResources.getDrawable(applicationContext, background)
            }

            fun bind(task: Task) {
                this.task = task
                if (task.isSolved == "true" && task.id == currentTaskIndex) {
                    taskBlock.background = AppCompatResources.getDrawable(
                        applicationContext,
                        R.drawable.task_solved_selected
                    )
                    oldItem = this
                    return
                }
                if (task.isSolved == "false" && task.id == currentTaskIndex) {
                    oldItem = this
                    taskBlock.background = AppCompatResources.getDrawable(
                        applicationContext,
                        R.drawable.task_not_solved_selected
                    )
                    return
                }

                if (task.isSolved == "true") {
                    taskBlock.background = AppCompatResources.getDrawable(
                        applicationContext,
                        R.drawable.task_solved_block
                    )
                    return
                }
                taskBlock.background = AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.task_not_solved_block
                )
            }

            override fun onClick(p0: View?) {
                setTaskSelected(this)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_place, TaskFragment(this.task)).commit()
            }
        }
    }

    class LinkDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            val (_, task, isSolved, submodule, input, output, answer) = oldItem
            val (_, task1, isSolved1, submodule1, input1, output1, answer1) = newItem

            return task == task1 && isSolved == isSolved1 && submodule == submodule1 && input == input1 && output == output1 && answer == answer1
        }

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class TaskViewModel : ViewModel() {
    val courseRepository = CourseRepository.get()
    var oldSavedTaskId: LiveData<Int>? = null
    var newSavedTaskId: Int? = null
}
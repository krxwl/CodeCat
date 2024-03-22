package com.github.krxwl.codecat.activities

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.database.CourseRepository
import com.github.krxwl.codecat.databinding.TaskActivityBinding
import com.github.krxwl.codecat.entities.Task
import com.github.krxwl.codecat.fragments.TaskFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "TaskActivity"
private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKRE9PRExFIiwic3ViIjoiV1MtQVBJLVRPS0VOIiwiY2xpZW50LWlkIjoiOGEzMTFiYTRjYzFiOGU3MGQxZmY1N2I1ZDQyYmZiYWMiLCJpYXQiOjE3MDU3NTY3NTgsImV4cCI6MTcwNTc1NjkzOH0.wjaz9_FEVeQbie1Lq3yX5zbIAIfq8l-vjtklPXi_sHY"


class TaskActivity : AppCompatActivity() {

    private lateinit var binding: TaskActivityBinding
    private lateinit var adapter: TaskAdapter

    private val taskViewModel: TaskViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TaskActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel.submoduleId = intent.extras?.getInt("submoduleId")!!
        taskViewModel.tasksLiveData = taskViewModel.getTasks(taskViewModel.submoduleId)



        taskViewModel.tasksLiveData?.observe(this
        ) { tasks ->
            taskViewModel.tasksArrayList = tasks as ArrayList<Task>
            taskViewModel.getDefaultTaskId(taskViewModel.submoduleId)

            taskViewModel.oldSavedTaskId?.observe(this) { defaultTask ->
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_place)
                Log.i(TAG, taskViewModel.tasksArrayList.toString())
                if (defaultTask != null) {
                    adapter = TaskAdapter(taskViewModel.tasksArrayList, defaultTask)
                    var currentTask: Task? = null
                    for (task in taskViewModel.tasksArrayList) {
                        if (task.id == defaultTask) {
                            currentTask = task
                        }
                    }
                    Log.i(TAG, "${currentTask}")
                    if (currentFragment == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_place, TaskFragment(currentTask!!)).commit()
                    }
                } else {
                    adapter = TaskAdapter(
                        taskViewModel.tasksArrayList,
                        taskViewModel.tasksArrayList.get(0).id
                    )

                    if (currentFragment == null) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_place, TaskFragment(taskViewModel.tasksArrayList[0]))
                            .commit()
                    }
                }
                binding.taskRecyclerview.adapter = adapter
            }
        }
        binding.taskRecyclerview.layoutManager = LinearLayoutManager(applicationContext)


    }

    override fun onStop() {
        // запоминаем в бд какое задание было открыто последним
        super.onStop()
        if (taskViewModel.newSavedTaskId != taskViewModel.oldSavedTaskId?.value && taskViewModel.newSavedTaskId != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                taskViewModel.commitSavedTaskId(taskViewModel.submoduleId, taskViewModel.newSavedTaskId!!)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        /*val i = Intent(this, MainActivity::class.java)
        startActivity(i)*/
        finish()
        return super.onKeyDown(keyCode, event)
    }

    inner class TaskAdapter(var tasks: ArrayList<Task>, var currentTaskIndex: Int = 0) : ListAdapter<Task, TaskAdapter.TaskHolder>(
        LinkDiffCallback()
    ) {

        lateinit var oldItem: TaskHolder

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        fun setTaskSelected(newView: TaskHolder) {
            if (oldItem.getTask().isSolved == 1) {
                oldItem.setTaskBlockBackground(R.drawable.task_solved_block)
            } else if (oldItem.getTask().isSolved == null) {
                oldItem.setTaskBlockBackground(R.drawable.task_not_solved_block)
            }
            if (newView.getTask().isSolved == 1) {
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

        inner class TaskHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

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
                Log.i(TAG, "${task.isSolved}")
                if (task.isSolved == 1 && task.id == currentTaskIndex) {
                    taskBlock.background = AppCompatResources.getDrawable(
                        applicationContext,
                        R.drawable.task_solved_selected
                    )
                    oldItem = this
                    return
                }
                if (task.isSolved == null && task.id == currentTaskIndex) {
                    oldItem = this
                    taskBlock.background = AppCompatResources.getDrawable(
                        applicationContext,
                        R.drawable.task_not_solved_selected
                    )
                    return
                }

                if (task.isSolved == 1) {
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
    private val courseRepository = CourseRepository.get()
    var tasksLiveData: LiveData<List<Task>>? = null
    var tasksArrayList: ArrayList<Task> = arrayListOf()
    var submoduleId: Int = 0
    var oldSavedTaskId: LiveData<Int>? = null
    var newSavedTaskId: Int? = null
    fun getDefaultTaskId(courseId: Int) {
        oldSavedTaskId = courseRepository.getDefaultTaskId(courseId)
    }

    fun getTasks(id: Int) = courseRepository.getTasks(id)

    fun commitSavedTaskId(submoduleId: Int, taskId: Int) = courseRepository.commitSavedTaskId(submoduleId, taskId)
}


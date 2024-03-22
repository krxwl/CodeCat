package com.github.krxwl.codecat.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.github.krxwl.codecat.entities.Course
import com.github.krxwl.codecat.entities.Submodule
import com.github.krxwl.codecat.entities.Task
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "databases.db"
class CourseRepository private constructor(context: Context) {

    private val database : CourseDatabase = Room.databaseBuilder(
        context.applicationContext,
        CourseDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset("databases.db").build()

    private val courseDao = database.courseDao()

    fun updateCourse(course: Course) = courseDao.updateCourse(course)

    fun getCourses(): LiveData<List<Course>> = courseDao.getCourses()


    fun getDefaultTaskId(id: Int): LiveData<Int>?=courseDao.getDefaultTaskId(id)

    fun commitSavedTaskId(submoduleId: Int, savedTaskId: Int) = courseDao.commitSavedTaskId(submoduleId, savedTaskId)

    fun getTasks(id: Int): LiveData<List<Task>> =courseDao.getTasks(id)


    fun getSubmodules(id: Int): LiveData<List<Submodule>> =courseDao.getSubmodules(id)

    fun getCourse(id: Int): LiveData<Course?> = courseDao.getCourse(id)

    companion object {
        // сам репозиторий
        private var INSTANCE: CourseRepository? = null

        // если еще не создан то создаем репозиторий
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CourseRepository(context)
            }
        }

        fun get(): CourseRepository {
            return INSTANCE ?:
            throw IllegalStateException("CourseRepo must be init")
        }
    }
}
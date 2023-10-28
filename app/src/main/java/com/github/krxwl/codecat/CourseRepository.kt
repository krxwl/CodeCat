package com.github.krxwl.codecat

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.github.krxwl.codecat.database.CourseDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "databases.db"
class CourseRepository private constructor(context: Context) {

    private val database : CourseDatabase = Room.databaseBuilder(
        context.applicationContext,
        CourseDatabase::class.java,
        DATABASE_NAME).build()

    private val courseDao = database.courseDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun updateCourse(course: Course) {
        executor.execute {
            courseDao.updateCourse(course)
        }
    }

    fun getCourses(): LiveData<List<Course>> {
        return courseDao.getCourses()
    }

    fun getSubmodules(id: Int): LiveData<List<Submodule>> {
        return courseDao.getSubmodules(id)
    }

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
package com.github.krxwl.codecat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.github.krxwl.codecat.entities.Course
import com.github.krxwl.codecat.entities.Submodule
import com.github.krxwl.codecat.entities.Task

@Dao
interface CourseDao {
    @Query("SELECT * FROM modules")
    fun getCourses(): LiveData<List<Course>>

    @Query("SELECT * FROM questions where submodule=(:id)")
    fun getTasks(id: Int): LiveData<List<Task>>

    @Query("UPDATE submodules SET savedTaskId=(:savedTaskId) WHERE id=(:submoduleId)")
    fun commitSavedTaskId(submoduleId: Int, savedTaskId: Int)

    @Query("SELECT savedTaskId FROM submodules where id=(:id)")
    fun getDefaultTaskId(id: Int): LiveData<Int>?

    @Query("SELECT * FROM submodules where module=(:id)")
    fun getSubmodules(id: Int): LiveData<List<Submodule>>

    @Query("SELECT * FROM modules WHERE id=(:id)")
    fun getCourse(id: Int): LiveData<Course?>

    @Update
    fun updateCourse(course: Course)
}
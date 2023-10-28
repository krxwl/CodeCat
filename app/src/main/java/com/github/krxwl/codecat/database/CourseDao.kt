package com.github.krxwl.codecat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.github.krxwl.codecat.Course
import com.github.krxwl.codecat.Submodule
import java.util.UUID

@Dao
interface CourseDao {
    @Query("SELECT * FROM modules")
    fun getCourses(): LiveData<List<Course>>

    @Query("SELECT * FROM submodules where module=(:id)")
    fun getSubmodules(id: Int): LiveData<List<Submodule>>

    @Query("SELECT * FROM modules WHERE id=(:id)")
    fun getCourse(id: Int): LiveData<Course?>

    @Update
    fun updateCourse(course: Course)
}
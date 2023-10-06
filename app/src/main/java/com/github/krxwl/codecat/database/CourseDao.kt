package com.github.krxwl.codecat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.github.krxwl.codecat.Course
import java.util.UUID

@Dao
interface CourseDao {
    @Query("SELECT * FROM modules")
    fun getCourses(): LiveData<List<Course>>

    @Query("SELECT * FROM modules WHERE id=(:id)")
    fun getCourse(id: UUID): LiveData<Course?>

    @Update
    fun updateCourse(course: Course)
}
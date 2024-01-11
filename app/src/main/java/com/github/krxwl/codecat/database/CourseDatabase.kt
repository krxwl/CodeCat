package com.github.krxwl.codecat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.krxwl.codecat.entities.Course
import com.github.krxwl.codecat.entities.Submodule
import com.github.krxwl.codecat.entities.Task
import com.github.krxwl.codecat.entities.Type

@Database(entities = [Course::class, Submodule::class, Type::class, Task::class], version = 2)
@TypeConverters(CourseTypeConverters::class)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDao
}

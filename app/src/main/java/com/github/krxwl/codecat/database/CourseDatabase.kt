package com.github.krxwl.codecat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.krxwl.codecat.Course
import com.github.krxwl.codecat.Submodule
import com.github.krxwl.codecat.Type

@Database(entities = [Course::class, Submodule::class, Type::class], version = 2)
@TypeConverters(CourseTypeConverters::class)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDao
}

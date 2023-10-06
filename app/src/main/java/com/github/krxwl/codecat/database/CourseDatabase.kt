package com.github.krxwl.codecat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.krxwl.codecat.Course

@Database(entities = [Course::class], version = 2)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
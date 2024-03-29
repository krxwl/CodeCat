package com.github.krxwl.codecat

import android.app.Application
import android.util.Log
import com.github.krxwl.codecat.database.CourseRepository

class CodeCatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CourseRepository.initialize(this)
    }

}
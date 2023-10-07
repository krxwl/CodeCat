package com.github.krxwl.codecat

import android.app.Application

class CodeCatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CourseRepository.initialize(this)
    }

}
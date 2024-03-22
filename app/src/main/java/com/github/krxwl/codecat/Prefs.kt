package com.github.krxwl.codecat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class Prefs {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")
        val MAIN_COURSE_KEY = intPreferencesKey("mainCourse")
        const val KEY_EMAIL_TEXT = "emailText"
        const val KEY_PASSWORD_TEXT = "passwordText"
        const val TAG = "SecondQuestionFragment"
    }
}

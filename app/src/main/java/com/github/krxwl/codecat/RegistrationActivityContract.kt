package com.github.krxwl.codecat

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

// контракт для запуска активити с результатом
class RegistrationActivityContract : ActivityResultContract<ArrayList<String>?, Boolean>() {
    override fun createIntent(context: Context, input: ArrayList<String>?): Intent {
        return Intent(context, RegistrationActivity::class.java)
            .putExtra("email_password_key", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean = when {
        resultCode != Activity.RESULT_OK -> false
        else -> intent?.getBooleanExtra("com.github.krxwl.codecat.LoginActivity.LOGIN_RESULT_KEY", true)!!
    }
}
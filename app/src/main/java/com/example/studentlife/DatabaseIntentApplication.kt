package com.example.studentlife

import android.app.Application
import android.util.Log

private val  TAG = "DatabaseIntentApplication"
class DatabaseIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        QuizRepository.initialize(this)
        Log.d(TAG,"Quiz initialize")


    }
}
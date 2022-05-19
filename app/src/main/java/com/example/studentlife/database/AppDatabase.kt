package com.example.studentlife.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studentlife.Question
import com.example.studentlife.Quiz

@Database(entities = [Quiz::class,Question::class ], version=1)


@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun  quizDao(): QuizDao

}



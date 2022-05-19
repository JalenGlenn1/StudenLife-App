package com.example.studentlife

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.studentlife.database.AppDatabase
import com.example.studentlife.database.QuizDao
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


class QuizRepositoryTest {

    private lateinit var db: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).allowMainThreadQueries() .build()


    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testSaveQuizWithQuestions() {

        val quizRepository = QuizRepository.get()
        val quiz = Quiz(mode = "easy", subject = "addition")
        val quizID = quiz.id
        var questions = mutableListOf<Question>()
        for (i in 1..10) {

            val prompt = "$i + $i"
            val answer = i + i

            val question = Question(prompt = prompt,quiz_id = quizID,correct_answer = answer.toString())
            questions.add(question)

        }
        quizRepository.saveQuizWithQuestions(quiz, questions)

        Log.d("testSaveQuizWithQuestions",quizRepository.getQuizQuestions(quizID).getOrAwaitValue().toString())

        Assert.assertNotEquals(quizRepository.getQuizQuestions(quizID).getOrAwaitValue().questions.size,0)
    }
}
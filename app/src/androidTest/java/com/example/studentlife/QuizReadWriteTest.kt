package com.example.studentlife

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.studentlife.database.AppDatabase
import com.example.studentlife.database.QuizDao
import org.junit.*

import java.io.IOException
import java.util.*

class QuizReadWriteTest {

    private lateinit var quizDao: QuizDao
    private lateinit var db: AppDatabase


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()



    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).allowMainThreadQueries() .build()
        quizDao = db.quizDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeQuizAndRead()
    {
        val quiz = Quiz(mode = "easy", subject = "addition")
        quizDao.insertQuiz(quiz)
        Assert.assertEquals(quizDao.getQuizzes().getOrAwaitValue().size,1)
    }

    @Test
    fun writeQuestionsandRead() {

        val quiz = Quiz(mode = "easy", subject = "addition")
        quizDao.insertQuiz(quiz)
        val quizID = quiz.id
        for (i in 1..10) {

            val prompt = "$i + $i"
            val answer = i + i

            val question = Question(prompt = prompt,quiz_id = quizID,correct_answer = answer.toString())
            quizDao.insertQuestion(question)

        }

         Log.d("myTag",quizDao.getQuizQuestions(quizID).toString())

          Assert.assertEquals(quizDao.getQuizQuestions(quizID).getOrAwaitValue().questions.size,10)
    }







}
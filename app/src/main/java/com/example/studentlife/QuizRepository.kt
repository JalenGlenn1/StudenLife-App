package com.example.studentlife

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.studentlife.database.AppDatabase
import com.example.studentlife.database.QuizDao
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "quiz-database"
class QuizRepository private constructor(context: Context) {

    private val executor = Executors.newSingleThreadExecutor()

    private val database : AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val quizDao = database.quizDao()


    fun getQuizzes(): LiveData<List<Quiz>> {

        return quizDao.getQuizzes()
    }

    fun insertQuiz(quiz: Quiz) {

        executor.execute {
             quizDao.insertQuiz(quiz)

         }
        }

    fun insertQuestion(question: Question) {

        executor.execute {

            quizDao.insertQuestion(question)
        }
    }
   fun getQuizQuestions(id: UUID): LiveData<QuizQuestions> {

       return quizDao.getQuizQuestions(id)
   }
    fun saveQuizWithQuestions(quiz: Quiz,questions: List<Question>) {

       insertQuiz(quiz)

        for (question:Question in questions) {

            insertQuestion(question)
        }
    }

    fun getAvgScore(): LiveData<Double> {



        return quizDao.getAvgScore()

    }
    companion object {
            private var INSTANCE: QuizRepository? = null
            fun initialize(context: Context) {
                if (INSTANCE == null) {
                    INSTANCE = QuizRepository(context)
                }
            }
            fun  get():QuizRepository {
                return INSTANCE ?:
                throw IllegalStateException("QuizRepository must be initialized")
            }
        }
        }

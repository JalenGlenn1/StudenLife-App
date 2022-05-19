package com.example.studentlife.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentlife.Question
import com.example.studentlife.Quiz
import com.example.studentlife.QuizQuestions
import java.util.*

@Dao
interface QuizDao {

    @Transaction
    @Query("SELECT * FROM quiz Where id= (:id)")
    fun getQuizQuestions(id:UUID): LiveData<QuizQuestions>

    @Query("SELECT * FROM quiz")
    fun getQuizzes(): LiveData<List<Quiz>>

    @Query("SELECT AVG(score) FROM quiz")
    fun getAvgScore(): LiveData<Double>

    @Insert
    fun insertQuiz(quiz: Quiz)

    @Insert
    fun deleteQuiz(quiz: Quiz)

    @Insert
    fun insertQuestion(question: Question)

    @Delete
    fun deleteQuestion(question: Question)

}

package com.example.studentlife

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.util.*

class QuizListViewModel : ViewModel() {

    private val quizRepository = QuizRepository.get()

    fun getQuizzes(): LiveData<List<Quiz>> {

        return quizRepository.getQuizzes()
    }

    fun getAvgScore(): LiveData<Double> = quizRepository.getAvgScore()

    fun saveQuizWithQuestions(quiz: Quiz,questions: List<Question>) {

        quizRepository.saveQuizWithQuestions(quiz,questions)
    }

    fun getQuizQuestions(id: UUID): LiveData<QuizQuestions> {

        return quizRepository.getQuizQuestions(id)
    }
}
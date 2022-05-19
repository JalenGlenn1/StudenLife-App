package com.example.studentlife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class QuizDetailViewModel : ViewModel() {

    private val quizRepository = QuizRepository.get()

    private val quizIdLiveData = MutableLiveData<UUID>()
    var quizLiveData: LiveData<QuizQuestions> =
        Transformations.switchMap(quizIdLiveData) { quizId ->
            quizRepository.getQuizQuestions(quizId)
        }
    fun loadQuiz(quizId: UUID) {

         quizIdLiveData.value = quizId

    }
}
package com.example.studentlife

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import org.w3c.dom.Text
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_QUIZ_ID = "quiz_id"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var quiz_id: UUID? = null
    private var param2: String? = null
    private val quizDetailViewModel: QuizDetailViewModel by lazy {
        ViewModelProviders.of(this).get(QuizDetailViewModel::class.java)
    }
    private var index = 0

    private lateinit var quiz: QuizQuestions
    private lateinit var quiz_question: TextView
    private lateinit var your_answer:TextView
    private lateinit var correct_answer:TextView
    private lateinit var next_button: Button
lateinit var quiz_title: TextView
lateinit var prev_button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quiz_id = it.getSerializable(ARG_QUIZ_ID) as UUID

            quizDetailViewModel.loadQuiz(quiz_id!!)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        quiz_question = view.findViewById(R.id.quiz_question) as TextView
        your_answer = view.findViewById(R.id.your_answer) as TextView
        correct_answer = view.findViewById(R.id.correct_answer) as TextView
        next_button = view.findViewById(R.id.continue_button) as Button
        quiz_title = view.findViewById(R.id.quiz_title) as TextView
        prev_button = view.findViewById(R.id.prev_button) as Button



        quizDetailViewModel.quizLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { quiz ->

            quiz?.let {

                this.quiz = quiz
                updateUI()
            }
        })
        return view
    }

    override fun onStart() {
        super.onStart()

        next_button.apply {
            setOnClickListener() {
                index ++
                updateUI()
            }
        }

        prev_button.apply {
            setOnClickListener() {
                if (index > 0) {
                    index --
                    updateUI()
                }

                else {

                    Toast.makeText(this.context,"You are on the first question", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    fun updateUI() {

        if (quiz.questions.size <= index) {

            Toast.makeText(this.context,"End of quiz", Toast.LENGTH_LONG).show()
            return
        }
        val question = quiz.questions[index]
        quiz_title.text = "${quiz.quiz.subject.capitalize()} Quiz ${quiz.quiz.score}"


        quiz_question.text = question.prompt
        correct_answer.text ="Correct answer " + question.correct_answer

        if (question.answer.isBlank()) {

            your_answer.text = "No answer"
        }

        else {

            your_answer.text = "Your answer " + question.answer

        }




    }
    companion object {

        @JvmStatic
        fun newInstance(uuid: UUID) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_QUIZ_ID, uuid)
                }

            }
    }
}
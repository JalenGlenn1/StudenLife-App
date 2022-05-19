package com.example.studentlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.ceil
import kotlin.math.floor

class DivisionTest : AppCompatActivity() {

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var questionTextView : TextView
    private lateinit var answerEditText: EditText
    private lateinit var continueButton : Button
    private lateinit var countdownTextView : TextView
    private lateinit var homeButton : Button
    private lateinit var questionCounterr: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_division_test)

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        questionTextView = findViewById(R.id.question_textview)
        answerEditText = findViewById(R.id.answer_edittext)
        continueButton = findViewById(R.id.nextBtn)
        countdownTextView = findViewById(R.id.countdown_textview)
        homeButton = findViewById(R.id.homeBtn)
        questionCounterr = findViewById(R.id.questionCounter)


        //Remembering the subject, difficulty,
        //and isTimerActivatedBoolean selected.
        val selectedSubject = intent.getStringExtra("Subject")
        val selectedDifficulty = intent.getStringExtra("Difficulty")
        var isTimerActivatedBoolean = intent.getBooleanExtra("isTimerActivatedBoolean", false)

        var firstVariable = randomValueBasedOnDifficulty(selectedDifficulty)
        var secondVariable = randomValueBasedOnDifficulty(selectedDifficulty)
        var questionCounter = 1
        var num: Int
        var str: String
        var millisInFuture : Int
        var timerRanOutBoolean = true

        var divisionObject = produceWholeNumber(selectedDifficulty)
        questionTextView.text = "${divisionObject.firstVariable} / ${divisionObject.secondVariable}"

        val quiz = Quiz(
            mode = selectedDifficulty.toString(),
            subject = selectedSubject.toString(),
            score = 0.0
        )
        val quizRepository = QuizRepository.get()
        val quizID = quiz.id

        val listOfBooleans = mutableListOf<Boolean>()
        val questions = mutableListOf<Question>()

        //If statement executes if the Timer is enabled.
        if(isTimerActivatedBoolean) {

            //If the user selected Easy difficulty then the timer is set to 100 seconds.
            if(selectedDifficulty.equals("Easy")){
                millisInFuture = 101000
            }
            //If the user selected Medium difficulty then the timer is set to 80 seconds.
            else if(selectedDifficulty.equals("Medium")){
                millisInFuture = 80100
            }
            //If the user selected hard difficulty then the timer is set to 60 seconds.
            else{
                millisInFuture = 61000
            }

            object : CountDownTimer(millisInFuture.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val sec = millisUntilFinished / 1000 // % 60
                    countdownTextView.text = sec.toString()

                    if(questions.size == 10 || questions.size > 9){
                        cancel()
                        countdownTextView.text = ""
                    }
                }
                override fun onFinish() {

                    var correctCounter = 0

                    for(index in listOfBooleans){

                        if(index){
                            correctCounter++
                        }
                    }

                    questionTextView.text = "$correctCounter/10"
                    quiz.score = correctCounter.toDouble()
                    quizRepository.saveQuizWithQuestions(quiz, questions)
                    timerRanOutBoolean = false
                }
            }.start()
        }
        //Else statement executes if the Timer is disabled.
        else{
            countdownTextView.text = ""
        }

        continueButton.setOnClickListener {

            if(timerRanOutBoolean) {
                //If statement runs if the user enters the correct answer
                if (divisionObject.answerVariable.toInt() == answerEditText.text.toString().toDouble().toInt()) {
                    listOfBooleans.add(true)
                }
                //Else statement runs if the user enters the wrong answer.
                else{
                    listOfBooleans.add(false)
                }

                val c = Question(
                    prompt = questionTextView.text.toString(),
                    answer = answerEditText.text.toString(),
                    correct_answer = divisionObject.answerVariable.toInt().toString(),
                    quiz_id = quizID
                )

                questions.add(c)

                //If statement runs if the user has answered 10 questions.
                if(questions.size == 10){

                    var correctCounter = 0

                    for(index in listOfBooleans){

                        if(index){
                            correctCounter++
                        }
                    }

                    questionTextView.text = "$correctCounter/10"
                    quiz.score = correctCounter.toDouble()
                    quizRepository.saveQuizWithQuestions(quiz, questions)
                }
                //Else statement runs if the user has not yet answered 10 questions.
                else{

                    if(questions.size < 11){
                        divisionObject = produceWholeNumber(selectedDifficulty)
                        questionTextView.text = "${divisionObject.firstVariable} / ${divisionObject.secondVariable}"
                        answerEditText.text.clear()
                        questionCounter++
                        questionCounterr.text = questionCounter.toString()
                    }
                }
            }
        }

        answerEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Enables the "NEXT" button when there is text in the EditText.
                if(answerEditText.text.isNotBlank()){
                    continueButton.isEnabled = true
                }
                //Disables the "NEXT" button when there is no text in the EditText.
                if(answerEditText.text.isBlank()){
                    continueButton.isEnabled = false
                }
                //This block of code was added to keep
                //it from crashing when backspacing.
                str = answerEditText.text.toString()
                if(str != ""){
                    num = Integer.parseInt(str)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        homeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun produceWholeNumber(selectedDifficulty: String?): DivisionObject {

        var a: Int
        var b: Int
        var answerVar: Double

        while (true) {

            a = randomValueBasedOnDifficulty(selectedDifficulty)
            b = (1..9).random()
            answerVar = (a.toDouble() / b.toDouble())

            if (ceil((answerVar)) == floor((answerVar))) {
                break
            }
        }

        return DivisionObject(
            firstVariable = a,
            secondVariable = b,
            answerVariable = answerVar
        )
    }

    //This function is used to determine which numbers will be
    //used for the addition test based on the difficulty set.
    private fun randomValueBasedOnDifficulty(selectedDifficulty: String?): Int {

        if(selectedDifficulty.equals("Easy")) {
            return (0..9).random()
        }
        else if(selectedDifficulty.equals("Medium")){
            return (10..99).random()
        }
        else{//Executes if the difficulty is set to Hard.
            return (100..999).random()
        }
    }
}
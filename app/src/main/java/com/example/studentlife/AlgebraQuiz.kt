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

class AlgebraQuiz : AppCompatActivity() {

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var questionTextView : TextView
    private lateinit var answerEditText: EditText
    private lateinit var continueButton : Button
    private lateinit var countdownTextView : TextView
    private lateinit var homeButton : Button
    private lateinit var questionCounterr: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algebra_qui)

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

        var num: Int
        var str: String
        var millisInFuture : Int
        var questionCounter = 1
        var timerRanOutBoolean = true

        val quiz = Quiz(
            mode = selectedDifficulty.toString(),
            subject = selectedSubject.toString(),
            score = 0.0
        )
        val quizRepository = QuizRepository.get()
        val quizID = quiz.id

        val listOfBooleans = mutableListOf<Boolean>()
        val questions = mutableListOf<Question>()

        var firstVariable = randomValueBasedOnDifficulty(selectedDifficulty)
        var secondVariable = randomValueBasedOnDifficulty(selectedDifficulty)

        var answerVariable = firstVariable + secondVariable
        questionTextView.text = "$firstVariable + X = $answerVariable"

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

            if(timerRanOutBoolean){
                //If statement runs if the user enters the correct answer.
                if (secondVariable == answerEditText.text.toString().toInt()){
                    listOfBooleans.add(true)
                }
                //Else statement runs if the user enters the wrong answer.
                else{
                    listOfBooleans.add(false)
                }

                val c = Question(
                    prompt = questionTextView.text.toString(),
                    answer = answerEditText.text.toString(),
                    correct_answer = secondVariable.toString(),
                    quiz_id = quizID
                )

                questions.add(c)

                //If statement runs if the user has answered 10 questions.
                if(questionCounter == 10){

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
                        questionCounter++
                        questionCounterr.text = questionCounter.toString()
                        firstVariable = randomValueBasedOnDifficulty(selectedDifficulty)
                        secondVariable = randomValueBasedOnDifficulty(selectedDifficulty)
                        answerVariable = firstVariable + secondVariable
                        questionTextView.text = "$firstVariable + X = $answerVariable"
                        answerEditText.text.clear()
                    }
                }
            }
        }

        answerEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Enables the "NEXT" button when there is text in the EditText.
                if(answerEditText.text.isNotEmpty()){
                    continueButton.isEnabled = true
                }
                //Disables the "NEXT" button when there is no text in the EditText.
                if(answerEditText.text.isEmpty()){
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
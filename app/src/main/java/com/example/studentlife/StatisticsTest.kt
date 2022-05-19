package com.example.studentlife

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class StatisticsTest : AppCompatActivity() {


    private lateinit var continueButton: Button
    private lateinit var questionBox: TextView
    private lateinit var editAnswer: EditText
    private lateinit var countdownTextView : TextView
    private lateinit var homeButton: Button
    private lateinit var questionCounterr: TextView

    //@SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_layout)

        continueButton = findViewById(R.id.nextBtn)
        questionBox = findViewById(R.id.question_textview)
        editAnswer = findViewById(R.id.answer_edittext)
        countdownTextView = findViewById(R.id.countdown_textview)
        homeButton = findViewById(R.id.homeBtn)
        questionCounterr = findViewById(R.id.questionCounter)

        val selectedSubject = intent.getStringExtra("Subject")
        val selectedDifficulty = intent.getStringExtra("Difficulty")
        var isTimerActivatedBoolean = intent.getBooleanExtra("isTimerActivatedBoolean", false)

        //val selectedDifficulty = intent.getStringExtra("Difficulty")
        //val selectedSubject = intent.getStringExtra("Subject")

        var numberSet = randomValues(selectedDifficulty.toString())
        var answer = 0
        var questionCounter = 1

        //var correctCounter = 0
        var millisInFuture : Int
        var timerRanOutBoolean = true

        var num: Int
        var str: String

        var determineNextQuestion = Random.nextInt(1,4)

        if (determineNextQuestion == 1)
        {
            questionBox.text = generateMeanQuestion(numberSet as MutableList<Int>)
            answer = meanAnswer(numberSet)
        }
        else if (determineNextQuestion == 2)
        {
            questionBox.text = generateModeQuestion(numberSet as MutableList<Int>)
            answer = modeAnswer(numberSet)
        }
        else
        {
            questionBox.text = generateMedianQuestion(numberSet as MutableList<Int>)
            answer = medianAnswer(numberSet)
        }


        val quiz = Quiz(
            mode = selectedDifficulty.toString(),
            subject = selectedSubject.toString(),
            score = 0.0
        )
        val quizRepository = QuizRepository.get()
        val quizID = quiz.id

        val listOfBooleans = mutableListOf<Boolean>()
        val questions = mutableListOf<Question>()

        val listOfQuestionObjects = mutableListOf<QuestionClass>()

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

                    questionBox.text = "$correctCounter/10"
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


        continueButton.setOnClickListener()
        {
            if(timerRanOutBoolean){

                if (answer == editAnswer.text.toString().toInt())
                {
                    listOfBooleans.add(true)
                }
                else
                {
                    listOfBooleans.add(false)
                }

                val c = Question(
                    prompt = questionBox.text.toString(),
                    answer = editAnswer.text.toString(),
                    correct_answer = answer.toString(),
                    quiz_id = quizID
                )

                questions.add(c)

                if (questionCounter == 10)
                {
                    var correctCounter = 0

                    for (i in listOfBooleans)
                    {
                        if (i)
                        {
                            correctCounter++
                        }
                    }
                    questionBox.text = "$correctCounter/10"
                    quiz.score = correctCounter.toDouble()
                    quizRepository.saveQuizWithQuestions(quiz, questions)
                }
                else
                {

                    if(questions.size < 11){
                        numberSet = randomValues(selectedDifficulty.toString())
                        editAnswer.text.clear()
                        determineNextQuestion = Random.nextInt(1,4)
                        if (determineNextQuestion == 1)
                        {
                            questionBox.text = generateMeanQuestion(numberSet as MutableList<Int>)
                            answer = meanAnswer(numberSet as MutableList<Int>)
                        }
                        else if (determineNextQuestion == 2)
                        {
                            questionBox.text = generateModeQuestion(numberSet as MutableList<Int>)
                            answer = modeAnswer(numberSet as MutableList<Int>)
                        }
                        else
                        {
                            questionBox.text = generateMedianQuestion(numberSet as MutableList<Int>)
                            answer = medianAnswer(numberSet as MutableList<Int>)
                        }
                        questionCounter++
                        questionCounterr.text = questionCounter.toString()
                    }
                }
            }
        }

        editAnswer.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Enables the "NEXT" button when there is text in the EditText.
                if(editAnswer.text.isNotEmpty()){
                    continueButton.isEnabled = true
                }
                //Disables the "NEXT" button when there is no text in the EditText.
                if(editAnswer.text.isEmpty()){
                    continueButton.isEnabled = false
                }
                //This block of code was added to keep
                //it from crashing when backspacing.
                str = editAnswer.text.toString()
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
}


// Generates a random mean question that will always have a whole number answer
fun generateMeanQuestion(randomValues: MutableList<Int>): String
{
    val listSize = randomValues.size
    // Adds one to the last value of the list until the mean of the list is a whole number
    while (randomValues.sum()%listSize != 0)
    {
        randomValues[listSize-1] = randomValues[listSize-1]+1
    }
    return "What is the mean of $randomValues"
}

// Generates a random median question that will always have a whole number answer
fun generateMedianQuestion(randomValues: MutableList<Int>): String
{
    val listSize = randomValues.size
    randomValues.sortDescending()
    // If its even, the nested if statements will make sure the middle two numbers average to a whole number
    if (listSize%2==0)
    {
        if ((randomValues[listSize/2]+randomValues[(listSize/2)-1])%2!=0)
        {
            randomValues.set(listSize/2,randomValues[listSize/2]+1)
        }
    }
    randomValues.shuffle()
    return "What is the median of $randomValues"
}

// Generates a random mode question
fun generateModeQuestion(randomValues: MutableList<Int>): String
{
    return "What is the mode of $randomValues"
}

fun meanAnswer(randomValues: MutableList<Int>): Int
{
    var sum = 0
    var average = 0
    for (i in randomValues)
    {
        sum += i
    }
    average = sum/randomValues.size
    return average
}

fun medianAnswer(randomValues: MutableList<Int>): Int
{
    val listSize = randomValues.size
    var sumOfMiddles = 0
    if (listSize%2==0)
    {
        sumOfMiddles = randomValues[listSize/2] + randomValues[listSize/2-1]
        return sumOfMiddles/2
    }
    else
    {
        return randomValues[listSize/2-1]
    }

}

fun modeAnswer(randomValues: MutableList<Int>): Int
{
    return randomValues.maxOrNull()!!
}

// Generates a random list of ints between 1 and 99 and can contain between 3 and 7 values
// Will be used for all types of questions
fun randomValues(difficulty: String): List<Int>
{
    if (difficulty == "Easy")
    {
        val start = Random.nextInt(1,3)
        val end = Random.nextInt(6,9)
        return List(Random.nextInt(3,7)) { Random.nextInt(start,end) }
    }
    else if (difficulty == "Medium")
    {
        val start = Random.nextInt(1,50)
        val end = Random.nextInt(50,99)
        return List(Random.nextInt(3,7)) { Random.nextInt(start,end) }
    }
    else
    {
        val start = Random.nextInt(100,500)
        val end = Random.nextInt(500,999)
        return List(Random.nextInt(3,7)) { Random.nextInt(start,end) }
    }
}
package com.example.studentlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TimerSelector : AppCompatActivity() {

    //Initialize all objects on the android
    //screen we will be interacting with.
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_selector)

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        noButton = findViewById(R.id.no_button)
        yesButton = findViewById(R.id.yes_button)

        //Remembering the subject and difficulty selected.
        val subject = intent.getStringExtra("Subject")
        val difficulty = intent.getStringExtra("Difficulty")
        var isTimerActivatedBoolean : Boolean

        noButton.setOnClickListener {

            isTimerActivatedBoolean = false
            openSubjectTest(subject, difficulty, isTimerActivatedBoolean)
        }

        yesButton.setOnClickListener{

            isTimerActivatedBoolean = true
            openSubjectTest(subject, difficulty, isTimerActivatedBoolean)
        }

    }

    //This function is used to initialize the next activity
    //and remembers the subject, difficulty, and the
    //isTimerActivatedBoolean to pass on to the next activity.
    private fun openSubjectTest(
        subject: String?,
        difficulty: String?,
        isTimerActivatedBoolean: Boolean
    ) {

        if(subject.equals("Addition")){
            val intent = Intent(this, AdditionTest::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            intent.putExtra("isTimerActivatedBoolean", isTimerActivatedBoolean)
            startActivity(intent)
        }

        if(subject.equals("Subtract", true)){
            val intent = Intent(this, SubtractionTest::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            intent.putExtra("isTimerActivatedBoolean", isTimerActivatedBoolean)
            startActivity(intent)
        }

        if(subject.equals("Algebra", true)){
            val intent = Intent(this, AlgebraQuiz::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            intent.putExtra("isTimerActivatedBoolean", isTimerActivatedBoolean)
            startActivity(intent)
        }

        if(subject.equals("Statistics", true)){
            val intent = Intent(this, StatisticsTest::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            intent.putExtra("isTimerActivatedBoolean", isTimerActivatedBoolean)
            startActivity(intent)
        }

        if(subject.equals("Division", true)){
            val intent = Intent(this, DivisionTest::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            intent.putExtra("isTimerActivatedBoolean", isTimerActivatedBoolean)
            startActivity(intent)
        }

        if(subject.equals("Multiplication", true)){
            val intent = Intent(this, MultiplicationTest::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            intent.putExtra("isTimerActivatedBoolean", isTimerActivatedBoolean)
            startActivity(intent)
        }
    }
}
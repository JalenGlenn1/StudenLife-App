package com.example.studentlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class DifficultySelector : AppCompatActivity() {

    //Initialize all objects on the android
    //screen we will be interacting with.
    private lateinit var easyButton : ImageButton
    private lateinit var mediumButton : ImageButton
    private lateinit var hardButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_selector)

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        easyButton = findViewById(R.id.easy_image_button)
        mediumButton = findViewById(R.id.medium_image_button)
        hardButton = findViewById(R.id.hard_image_button)

        //Remembering the subject selected.
        val subject = intent.getStringExtra("Subject")
        var difficulty : String

        //The following code executes when users click the "Easy" button.
        easyButton.setOnClickListener{
            difficulty = "Easy"
            val intent = Intent(this, TimerSelector::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            startActivity(intent)
        }

        //The following code executes when users click the "Medium" button.
        mediumButton.setOnClickListener{
            difficulty = "Medium"
            val intent = Intent(this, TimerSelector::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            startActivity(intent)
        }

        //The following code executes when users click the "Hard" button.
        hardButton.setOnClickListener{
            difficulty = "Hard"
            val intent = Intent(this, TimerSelector::class.java)
            intent.putExtra("Subject", subject)
            intent.putExtra("Difficulty", difficulty)
            startActivity(intent)
        }
    }
}
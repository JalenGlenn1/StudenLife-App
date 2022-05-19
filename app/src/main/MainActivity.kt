package com.bitdev.multidivide

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.math.round

class MainActivity : AppCompatActivity() {


    private lateinit var multiplicationButton: Button
    private lateinit var divisionButton: Button
    private lateinit var levelEasyButton: Button
    private lateinit var levelMediumButton: Button
    private lateinit var levelHardButton: Button
    private lateinit var cancelButton: Button
    private lateinit var level: TextView

    private var correctedAnswers: Int = 0
    private var type = ""
    private var result = 0.0

    private lateinit var gameLayout: ConstraintLayout

    private lateinit var checkResultButton: Button
    private lateinit var answer: EditText
    private lateinit var equationText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        multiplicationButton = findViewById(R.id.multiplication)
        divisionButton = findViewById(R.id.division)

        levelEasyButton = findViewById(R.id.levelEasy)
        levelMediumButton = findViewById(R.id.levelMedium)
        levelHardButton = findViewById(R.id.levelHard)

        cancelButton = findViewById(R.id.cancel)

        level = findViewById(R.id.level)

        gameLayout = findViewById(R.id.game)
        answer = findViewById(R.id.result)
        equationText = findViewById(R.id.equation)
        checkResultButton = findViewById(R.id.check_result_button)


        multiplicationButton.setOnClickListener {
            type = "multiplication"
            showLevels()
        }
        divisionButton.setOnClickListener {
            type = "division"
            showLevels()
        }

        levelEasyButton.setOnClickListener {
            correctedAnswers = 0
            showGame(1)
            level.text = 1.toString()
        }
        levelMediumButton.setOnClickListener {
            correctedAnswers = 0
            showGame(2)
            level.text = 2.toString()
        }
        levelHardButton.setOnClickListener {
            correctedAnswers = 0
            showGame(3)
            level.text = 3.toString()
        }
        cancelButton.setOnClickListener {
            correctedAnswers = 0
            resetGame()
        }

        checkResultButton.setOnClickListener {
            it.hideKeyboard()
            checkResult()
        }
    }

    fun isWhole(value: Double): Boolean {
        return value - value.toInt() == 0.0
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    private fun checkResult() {

        val answer = answer.text.toString()
        val resultDouble = result
        val isWhole = isWhole(resultDouble)
        val levelSelected = level.text.toString().toInt()

        if (answer.isNotEmpty()) {
            if (isWhole) {
                if (result.toInt().toString() == answer) {
                    correctedAnswers++
                    if (correctedAnswers < 10) {
                        Toast.makeText(this, "CORRECT ANSWER!", Toast.LENGTH_LONG).show()
                        showGame(levelSelected)
                    } else {
                        Toast.makeText(
                            this,
                            "YOU ANSWERED 10 CORRECTLY!",
                            Toast.LENGTH_LONG
                        ).show()
                        resetGame()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "WRONG ANSWER! CORRECT ANSWER WAS: ${result.toInt()}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    resetGame()
                }
            } else {
                if (result.round(2).toString() == answer) {
                    correctedAnswers++
                    if (correctedAnswers < 10) {
                        Toast.makeText(this, "CORRECT ANSWER!", Toast.LENGTH_LONG).show()
                        showGame(levelSelected)
                    } else {
                        Toast.makeText(
                            this,
                            "YOU ANSWERED 10 CORRECTLY!",
                            Toast.LENGTH_LONG
                        ).show()
                        resetGame()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "WRONG ANSWER! CORRECT ANSWER WAS: ${result.round(2)}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    resetGame()
                }
            }
        } else {
            Toast.makeText(this, "Type in your answer!", Toast.LENGTH_LONG).show()
        }
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun showGame(level: Int) {

        levelEasyButton.visibility = View.GONE
        levelHardButton.visibility = View.GONE
        levelMediumButton.visibility = View.GONE
        cancelButton.visibility = View.GONE

        answer.text = null
        gameLayout.visibility = View.VISIBLE

        var random1 = 0
        var random2 = 0
        result = 0.0

        if (level == 1) {
            random1 = (1..9).random()
            random2 = (1..9).random()
        } else if (level == 2) {
            random1 = (10..99).random()
            random2 = (10..99).random()
        } else {
            random1 = (100..999).random()
            random2 = (100..999).random()
        }

        if (type == "multiplication") {
            result = (random1 * random2).toDouble()
            equationText.text = "$random1 * $random2"
        } else if (type == "division") {
            result = random1.toDouble() / random2.toDouble()
            equationText.text = "$random1 / $random2"
        }

        Log.e("result", result.toString())
    }

    fun resetGame() {
        type = ""
        correctedAnswers = 0
        result = 0.0
        level.text = 0.toString()
        answer.text = null

        levelEasyButton.visibility = View.GONE
        levelHardButton.visibility = View.GONE
        levelMediumButton.visibility = View.GONE
        cancelButton.visibility = View.GONE


        multiplicationButton.visibility = View.VISIBLE
        divisionButton.visibility = View.VISIBLE

        gameLayout.visibility = View.GONE
    }

    fun showLevels() {
        levelEasyButton.visibility = View.VISIBLE
        levelHardButton.visibility = View.VISIBLE
        levelMediumButton.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE


        multiplicationButton.visibility = View.GONE
        divisionButton.visibility = View.GONE
    }
}

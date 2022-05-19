package com.example.studentlife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import java.util.*


const val PASTTEST_ID = "pasttest id"

class PastTest : AppCompatActivity(), QuizListFragment.Callbacks {
    private val newPastTestActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.acitivity_past_test)

        super.onCreate(savedInstanceState)
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        if (currentFragment == null) {
            val fragment = QuizListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_view, fragment)
                .commit()
        }
    }

    override fun onQuizSelect(uuid: UUID) {
        val fragment = QuizFragment.newInstance(uuid)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .commit()

    }

}
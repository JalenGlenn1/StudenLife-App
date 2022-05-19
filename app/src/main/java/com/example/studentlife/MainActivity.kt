package com.example.studentlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class  MainActivity : AppCompatActivity() {

    private lateinit var addButton: ImageButton
    private lateinit var subtractButton: ImageButton
    private lateinit var multiButton: ImageButton
    private lateinit var statsButton: ImageButton
    private lateinit var divisionButton : ImageButton
    private lateinit var algebraButton : ImageButton
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var progressView: View
    private lateinit var actionBarToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity_main)

        addButton = findViewById(R.id.addition_button)
        subtractButton = findViewById(R.id.subtraction_button)
        multiButton = findViewById(R.id.multi_button)
        statsButton = findViewById(R.id.stats_button)
        divisionButton = findViewById(R.id.division_button)
        algebraButton = findViewById(R.id.algebra_button)
        progressView = findViewById(R.id.progressView)

        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)

        //may not need this below either
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()

        //

        navView.setNavigationItemSelectedListener{ menuItem ->
            when (menuItem.itemId){

                R.id.nav_profile ->{
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_about ->{
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_settings->{
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }

        addButton.setOnClickListener {
            val intent = Intent(this, DifficultySelector::class.java)
            intent.putExtra("Subject", "Addition")
            startActivity(intent)
        }
        subtractButton.setOnClickListener {
            val intent = Intent(this, DifficultySelector::class.java)
            intent.putExtra("Subject", "Subtract")
            startActivity(intent)
        }
        multiButton.setOnClickListener{
            val intent = Intent(this, DifficultySelector::class.java)
            intent.putExtra("Subject", "Multiplication")
            startActivity(intent)
        }
        statsButton.setOnClickListener {
            val intent = Intent(this, DifficultySelector::class.java)
            intent.putExtra("Subject", "Statistics")
            startActivity(intent)
        }
        divisionButton.setOnClickListener {
            val intent = Intent(this, DifficultySelector::class.java)
            intent.putExtra("Subject", "Division")
            startActivity(intent)
        }
        algebraButton.setOnClickListener {
            val intent = Intent(this, DifficultySelector::class.java)
            intent.putExtra("Subject", "Algebra")
            startActivity(intent)
        }
        progressView.setOnClickListener{
            val intent = Intent(this, PastTest::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }
}
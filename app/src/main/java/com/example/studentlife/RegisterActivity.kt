package com.example.studentlife

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.time.format.TextStyle

class RegisterActivity: AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var name: TextView
    private lateinit var score: TextView
    private lateinit var registerButton: Button
    private lateinit var existingAccountView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account ...")
        progressDialog.setCanceledOnTouchOutside(false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("users")
        registerButton = findViewById(R.id.createButton)
        email = findViewById(R.id.emailText)
        password = findViewById(R.id.passwordText)
        name = findViewById(R.id.nameText)
        existingAccountView = findViewById(R.id.existingAccountView)


        register()

        existingAccountView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {

        registerButton.setOnClickListener {
            if (TextUtils.isEmpty(name.text.toString())) {
                name.error = "Please enter a username"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(email.text.toString())) {
                email.error = "Please enter an email"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password.text.toString())) {
                password.error = "Please enter an password"
                return@setOnClickListener
            } else if (password.length() < 6){
                password.error = "PASSWORD MUST BE AT LEAST 6 CHARACTERS"
            }

            progressDialog.show()

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressDialog.dismiss()
                        val currentUser = auth.currentUser
                        val currentUserdDb = databaseReference?.child((currentUser?.uid!!))
                        currentUserdDb?.child("email")?.setValue(email.text.toString())
                        currentUserdDb?.child("name")?.setValue(name.text.toString())

                        Toast.makeText(this, "ACCOUNT SUCCESSFULLY CREATED with email ${email.text.toString()}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "SIGN UP FAILED", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
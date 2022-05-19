package com.example.studentlife

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity(){

    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var loginBtn: Button
    private lateinit var createAccount: TextView
    private lateinit var progressDialog: ProgressDialog
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging in ...")
        progressDialog.setCanceledOnTouchOutside(false)

        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.emailText)
        password = findViewById(R.id.passwordText)
        loginBtn = findViewById(R.id.loginButton)
        createAccount = findViewById(R.id.createAccountView)

        val currentUser = auth.currentUser
        if(currentUser !=null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
        login()

    }

    private fun login(){

        loginBtn.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString())) {
                email.error = "Please enter an email"
                return@setOnClickListener
            }
            else if (TextUtils.isEmpty(password.text.toString())) {
                password.error = "Please enter a password"
                return@setOnClickListener
            }

            progressDialog.show()

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnSuccessListener {
                    progressDialog.dismiss()

                    val firebaseUser = auth.currentUser
                    val email = firebaseUser!!.email

                    Toast.makeText(this, "LOGGED IN AS $email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e->
                    progressDialog.dismiss()
                    Toast.makeText(this, "LOGIN FAILED DUE TO ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        createAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
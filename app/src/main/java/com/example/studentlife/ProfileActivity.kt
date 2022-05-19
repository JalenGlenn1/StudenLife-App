package com.example.studentlife

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.studentlife.databinding.ActivityProfileBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.w3c.dom.Text
import java.lang.ref.Reference

private lateinit var navView: NavigationView

class ProfileActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialogDelete: ProgressDialog
    private lateinit var cUser: TextView
    private lateinit var logoutBtn: TextView
    private lateinit var emailText: TextView
    private lateinit var homeBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var confirmBtn: Button
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var name: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_profile_activity)

        navView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener{ menuItem ->
            when (menuItem.itemId){
                R.id.nav_home ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_about ->{
                    Toast.makeText(this, "Edit ME", Toast.LENGTH_LONG).show()
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

        cUser = findViewById(R.id.signedinID)
        logoutBtn = findViewById(R.id.logoutTextview)
        emailText = findViewById(R.id.currentUser)
        deleteBtn = findViewById(R.id.deleteBtn)
        confirmBtn = findViewById(R.id.confirmButton)
        //working on this
        email = findViewById(R.id.emailUpdate)
        password = findViewById(R.id.passwordUpdate)
        name = findViewById(R.id.userUpdate)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("users")

        progressDialogDelete = ProgressDialog(this)
        progressDialogDelete.setTitle("Please Wait")
        progressDialogDelete.setMessage("Deleting Account ...")
        progressDialogDelete.setCanceledOnTouchOutside(false)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Signing Out ...")
        progressDialog.setCanceledOnTouchOutside(false)

        loadProfile()
    }

    private fun loadProfile() {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        //sets the email text to the textveiw
        //emailText.text = "Email: "+user?.email

        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                emailText.text = "Username: "+snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        logoutBtn.setOnClickListener {
            progressDialog.show()
            val toast = Toast.makeText(this, "Signed Out ", Toast.LENGTH_SHORT).show()
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            //checkUser()
            finish()
        }

        deleteBtn.setOnClickListener {
            progressDialogDelete.show()
            val toast = Toast.makeText(this, "Deleting Account ", Toast.LENGTH_SHORT).show()
            deleteUser()
            auth.signOut()
            //checkUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        confirmBtn.setOnClickListener {
            val toast = Toast.makeText(this, "Updating Account ", Toast.LENGTH_SHORT).show()
            updateUser()
            auth.signOut()
            //checkUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun deleteUser(){
        val user = FirebaseAuth.getInstance().currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "User account deleted", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUser(){

        val user = FirebaseAuth.getInstance().currentUser!!

        user!!.updateEmail(email.text.toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "User email updated", Toast.LENGTH_SHORT).show()

                }
            }
        user!!.updatePassword(password.text.toString().trim())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "User password updated", Toast.LENGTH_SHORT).show()
                }
            }
    }








    /*
    private lateinit var binding: ActivityProfileBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var progressDialogDelete: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private var email = ""
    private var password = ""
    private var name = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialogDelete = ProgressDialog(this)
        progressDialogDelete.setTitle("Please Wait")
        progressDialogDelete.setMessage("Deleting Account ...")
        progressDialogDelete.setCanceledOnTouchOutside(false)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Signing Out ...")
        progressDialog.setCanceledOnTouchOutside(false)
        //----------------------------------------------------------------------
        //IF YOU RUN INTO ERRORS LATER, IT'S PROBABLY THIS LINE OF CODE HERE
        //setContentView(R.layout.nav_profile_activity)
        //TWO SETCONTENTVIEWS ARE SET HERE WHICH MAY CAUSE ISSUES ^^^^^^^^^^^^^^^^^

        /*
        navView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener{ menuItem ->
            when (menuItem.itemId){
                R.id.nav_home ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_about ->{
                    Toast.makeText(this, "Edit ME", Toast.LENGTH_LONG).show()
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
        }*/
        //------------------------------------------------------------------------
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")


        homeBtn = findViewById(R.id.homeBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        confirmBtn = findViewById(R.id.confirmButton)


        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        deleteBtn.setOnClickListener {
            progressDialogDelete.show()
            val toast = Toast.makeText(this, "Deleting Account ", Toast.LENGTH_SHORT).show()
            deleteUser()
            firebaseAuth.signOut()
            checkUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        confirmBtn.setOnClickListener {
            val toast = Toast.makeText(this, "Updating Account ", Toast.LENGTH_SHORT).show()
            updateUser()
            firebaseAuth.signOut()
            checkUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }



        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        checkUser()

        binding.logoutTextview.setOnClickListener {
            progressDialog.show()
            val toast = Toast.makeText(this, "Signed Out ", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
            checkUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchUsers(){
        val userRef = database.collection("users")
        userRef.get().addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    Toast.makeText(this, "User fetch success", Toast.LENGTH_SHORT).show()

                    val users: ArrayList<User> = ArrayList()

                    for (document in it.result!!.documents) {
                        val user = document.toObject(User::class.java)!!
                        users.add(user)
                        print("Email: " + user.email)
                    }
                }
                else ->{
                    Toast.makeText(this, "User fetch failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteUser(){
        val user = FirebaseAuth.getInstance().currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "User account deleted", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUser(){
        email = binding.emailUpdate.text.toString().trim()
        password = binding.passwordUpdate.text.toString().trim()
        name = binding.userUpdate.text.toString().trim()

        val user = FirebaseAuth.getInstance().currentUser!!

        user!!.updateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "User email updated", Toast.LENGTH_SHORT).show()

                }
            }
        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "User password updated", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getData(){
        val user = Firebase.auth.currentUser
        user?.let{
            for(profile in it.providerData){
                val providerId = profile.providerId
                val uid = profile.uid

                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }

    private fun checkUser() {
        val currentUser = firebaseAuth.currentUser
        binding.currentUser.text = String.format("%s", currentUser?.email)

    }*/
}
package com.example.jobquest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.jobquest.MainActivity
import com.example.jobquest.R
import com.example.jobquest.fragments.SentApplicationsFragment
import com.example.jobquest.models.NewApplicationsModel
import com.example.jobquest.models.NewUserProfileModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        dbReference = FirebaseDatabase.getInstance().getReference("UserProfiles")

        val signInText: TextView = findViewById(R.id.textViewAlreadySignedUp)
        signInText.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.signUp)
        registerButton.setOnClickListener {
            performSignUp()
        }

    }

    private fun performSignUp() {
        val email = findViewById<EditText>(R.id.editTextSignUpEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)

        if (email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Please fill in all the fields.",Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener (this) { task->

            if (task.isSuccessful){
                saveUserData()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(baseContext,"Authentication successful.",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener{
                Toast.makeText(this,"Error occurred ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveUserData(){
        val uID: String = auth.currentUser?.uid.toString()

        // Get the values entered by the user
        val name = findViewById<EditText>(R.id.editTextSignUpPersonName).text.toString()
        val email = findViewById<EditText>(R.id.editTextSignUpEmail).text.toString()
        val userType = findViewById<Spinner>(R.id.userTypeSpinner).selectedItem.toString()
        //val city = findViewById<EditText>(R.id.editTextSignUpPersonCity).text.toString()


        if(name.isEmpty()){
            findViewById<EditText>(R.id.edit_text_name).error = "Please enter your Name. "
            return
        }
        if(userType.isEmpty()){
            findViewById<EditText>(R.id.edit_text_age).error = "Please enter your Age. "
            return
        }


        val newUser = NewUserProfileModel(uID, name, email, userType)
        dbReference.child(uID).setValue(newUser)
            .addOnCompleteListener {
                Toast.makeText(this, "Welcome...", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
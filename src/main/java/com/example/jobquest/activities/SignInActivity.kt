package com.example.jobquest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.jobquest.MainActivity
import com.example.jobquest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        val signUpText: TextView = findViewById(R.id.textViewNotSignedUp)
        signUpText.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.signIn)
        loginButton.setOnClickListener {
            performSignIn()
        }

    }

    private fun performSignIn() {
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.textPassword)

        if (email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this) { task->
            if (task.isSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(baseContext,"Login successful.",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
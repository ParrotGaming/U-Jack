package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val etEmailSignUp = findViewById<EditText>(R.id.etEmailSignUp)
        val etPasswordSignUp = findViewById<EditText>(R.id.etPasswordSignUp)

        btnSignUp.setOnClickListener() {
            auth.createUserWithEmailAndPassword(etEmailSignUp.text.toString(), etPasswordSignUp.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        etEmailSignUp.text.clear()
                        etPasswordSignUp.text.clear()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user
                        println("FAILED")
                    }
                }
        }
    }
}
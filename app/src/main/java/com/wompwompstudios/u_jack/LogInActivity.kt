package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnLogIn = findViewById<Button>(R.id.btnLogIn)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val etEmailLogIn = findViewById<EditText>(R.id.etEmailLogIn)
        val etPasswordLogIn = findViewById<EditText>(R.id.etPasswordLogIn)

        btnLogIn.setOnClickListener() {
            auth.signInWithEmailAndPassword(etEmailLogIn.text.toString(), etPasswordLogIn.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        etEmailLogIn.text.clear()
                        etPasswordLogIn.text.clear()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.

                    }
                }
        }
        btnCreateAccount.setOnClickListener() {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}
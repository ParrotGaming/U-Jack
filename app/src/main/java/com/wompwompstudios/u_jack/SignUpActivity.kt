package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        val db = Firebase.firestore
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
                        val theft = hashMapOf(
                            "Target" to 950,
                            "Walmart" to 950,
                            "Safeway" to 950,
                            "Trader Joe's" to 950,
                            "Undercaf" to 950
                        )
                        db.collection("thefts").document(auth.currentUser!!.uid)
                            .set(theft).addOnSuccessListener {
                                Log.d(Log.DEBUG.toString(), "Added user")
                            }.addOnFailureListener {e ->
                                Log.d(Log.ERROR.toString(), e.toString())
                            }
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user
                        println("FAILED")
                    }
                }
        }
    }
}
package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ReportActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val database = Firebase.firestore

        if(auth.currentUser == null) {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val btnSubmitReport = findViewById<Button>(R.id.btnSubmitReport)
        val etUserDescription = findViewById<EditText>(R.id.UserCarDescriptionInput)
        val etEstimatedValue = findViewById<EditText>(R.id.EstimatedCarValueInput)
        val etDifficultyRating = findViewById<EditText>(R.id.DifficultyRatingStatementInput)

        btnSubmitReport.setOnClickListener() {
            if(Integer.parseInt(etDifficultyRating.text.toString()) in 1..10) {
                val car = hashMapOf(
                    "User" to auth.currentUser!!.uid,
                    "Description" to etUserDescription.text.toString(),
                    "Value" to etEstimatedValue.text.toString(),
                    "Difficulty" to etDifficultyRating.text.toString(),
                )

                database.collection("cars")
                    .add(car)
                    .addOnSuccessListener { documentReference ->
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    .addOnFailureListener { e ->

                    }
            }
        }
    }
}
package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
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
        val carImageView = findViewById<ImageView>(R.id.CarImage)
        val carImagebtn = findViewById<Button>(R.id.btnAddImage)

        val changeImage =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    val imgUri = data?.data
                    carImageView.setImageURI(imgUri)
                }
            }

        carImagebtn.setOnClickListener {
            val picking = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(picking)
        }


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
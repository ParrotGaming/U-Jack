package com.wompwompstudios.u_jack

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class ReportCarActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //Content view
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_car)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val database = Firebase.firestore
        val storage = Firebase.storage("gs://u-jack.appspot.com")
        val storageRef = storage.reference
        var image: Uri? = null
        //View references
        val btnSubmitReport = findViewById<Button>(R.id.btnSubmitReport)
        val etUserDescription = findViewById<EditText>(R.id.UserCarDescriptionInput)
        val etEstimatedValue = findViewById<EditText>(R.id.EstimatedCarValueInput)
        val etDifficultyRating = findViewById<EditText>(R.id.DifficultyRatingStatementInput)
        val carImageView = findViewById<ImageView>(R.id.CarImage)
        val carImagebtn = findViewById<Button>(R.id.btnAddImage)

        //Btn submit logic
        btnSubmitReport.setOnClickListener() {
            //validate input
            if (Integer.parseInt(etDifficultyRating?.text.toString()) in 1..10) {
                //create car object
                var imageUrl = "https://placehold.co/400.png"
                if (image != null) {
                    val carRef = storageRef.child("images/${image!!.lastPathSegment}")
                    val uploadTask = carRef.putFile(image!!)

                    val urlTask = uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        carRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            imageUrl = task.result.toString()

                            val car = hashMapOf(
                                "User" to auth.currentUser!!.uid,
                                "Description" to etUserDescription?.text.toString(),
                                "Value" to etEstimatedValue?.text.toString(),
                                "Difficulty" to etDifficultyRating?.text.toString(),
                                "Location" to "37°46'38.7\"N 122°26'57.5\"W",
                                "Image" to imageUrl
                            )
                            //push to database
                            database.collection("cars")
                                .add(car)
                                .addOnSuccessListener { documentReference ->
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    println(e)
                                }
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                }
            }
        }

        val changeImage =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    val imgUri = data?.data
                    image = imgUri
                    carImageView.setImageURI(imgUri)
                }
            }

        carImagebtn.setOnClickListener {
            val picking = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(picking)
        }
    }
}
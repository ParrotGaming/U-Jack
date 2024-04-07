package com.wompwompstudios.u_jack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import okhttp3.internal.wait


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class  ReportFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        //Content view
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val database = Firebase.firestore
        val storage = Firebase.storage("gs://u-jack.appspot.com")
        val storageRef = storage.reference
        var image:Uri? = null
        //View references
        val btnSubmitReport = view.findViewById<Button>(R.id.btnSubmitReport)
        val etUserDescription = view.findViewById<EditText>(R.id.UserCarDescriptionInput)
        val etEstimatedValue = view.findViewById<EditText>(R.id.EstimatedCarValueInput)
        val etDifficultyRating = view.findViewById<EditText>(R.id.DifficultyRatingStatementInput)
        val carImageView = view.findViewById<ImageView>(R.id.CarImage)
        val carImagebtn = view.findViewById<Button>(R.id.btnAddImage)

        //Btn submit logic
        btnSubmitReport.setOnClickListener() {
            //validate input
            if(Integer.parseInt(etDifficultyRating?.text.toString()) in 1..10) {
                //create car object
                var imageUrl = "https://placehold.co/400.png"
                if(image != null) {
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
                                    (activity as MainActivity).replaceFragment(SearchFragment())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
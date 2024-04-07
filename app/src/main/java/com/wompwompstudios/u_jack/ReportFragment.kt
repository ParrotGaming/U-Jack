package com.wompwompstudios.u_jack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
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
                val car = hashMapOf(
                    "User" to auth.currentUser!!.uid,
                    "Description" to etUserDescription?.text.toString(),
                    "Value" to etEstimatedValue?.text.toString(),
                    "Difficulty" to etDifficultyRating?.text.toString(),
                )
                //push to database
                database.collection("cars")
                    .add(car)
                    .addOnSuccessListener { documentReference ->
                        MainActivity().replaceFragment(SearchFragment())
                    }
                    .addOnFailureListener { e ->
                        println(e)
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
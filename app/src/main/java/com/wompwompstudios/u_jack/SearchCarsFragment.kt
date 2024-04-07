package com.wompwompstudios.u_jack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchCarsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchCarsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        return inflater.inflate(R.layout.fragment_search_cars, container, false)
    }
    private lateinit var ImageAdaptor: ImageAdapter
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ImageAdaptor = ImageAdapter((activity as MainActivity).applicationContext, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

        auth = FirebaseAuth.getInstance()
        val database = Firebase.firestore

        val rvAdaptor = view.findViewById<RecyclerView>(R.id.rvListOfCars)
        rvAdaptor.adapter = ImageAdaptor
        rvAdaptor.layoutManager = LinearLayoutManager((activity as MainActivity))

        database.collection("cars")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    ImageAdaptor.addImage(Image(document.data["Image"].toString(), "test"), document.data["Description"].toString(), document.data["Value"].toString(), document.data["Difficulty"].toString(), document.data["User"].toString())
                }
            }
            .addOnFailureListener { exception ->

            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment search_cars.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchCarsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
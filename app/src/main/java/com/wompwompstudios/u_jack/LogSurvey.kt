package com.wompwompstudios.u_jack

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class LogSurvey : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theft_log_survey)
        val auth = FirebaseAuth.getInstance()
        val database = Firebase.firestore
        val storage = Firebase.storage("gs://u-jack.appspot.com")
        val storageRef = storage.reference

        // dropdown
        val dropdown : Spinner = findViewById(R.id.ddStoreID)
        val items = arrayOf("Target", "Walmart", "Safeway", "Trader Joe's", "Undercaf")
        val ddAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = ddAdapter

        // Btn
        val btn = findViewById<Button>(R.id.btnAddTheftLog)
        btn.setOnClickListener {
            val moneyInput  = findViewById<EditText>(R.id.AmountStolenInput)
            val money = moneyInput.text.toString().toInt()
            val store = dropdown.selectedItem.toString()
            Log.d(Log.INFO.toString(), "$money from $store")
/*
            val collection = database.collection("thefts").document(auth.currentUser!!.uid)
                .collection("Store")

            collection
                .get()
                .addOnSuccessListener {
                    documents ->
                    if (documents.isEmpty) {
                        Log.d(Log.DEBUG.toString(), "Creating new Value for Stores")
                        val values = hashMapOf(
                            store to money
                        )
                        Log.d(Log.INFO.toString(), values.toString())
                        collection
                            .add(values)
                            .addOnSuccessListener { finish() }
                            .addOnFailureListener {
                                e -> Log.d(Log.ERROR.toString(), e.toString())
                            }
                    } else {
                        Log.d(Log.DEBUG.toString(), "Existing value found for $store")
                        val doc = documents.documents[0]
                        val existingAmount = doc.get(store) as Long
                        val newAmount = existingAmount + money
                        doc.reference.update(store, newAmount)
                            .addOnSuccessListener { finish() }
                            .addOnFailureListener {
                                e -> Log.d(Log.ERROR.toString(), e.toString())
                            }
                    }
                }*/
            database.collection("thefts").document(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.data == null) {
                        val theft = hashMapOf(
                            "Target" to 0,
                            "Walmart" to 0,
                            "Safeway" to 0,
                            "Trader Joe's" to 0,
                            "Undercaf" to 0
                        )
                        database.collection("thefts").document(auth.currentUser!!.uid).set(theft)
                            .addOnSuccessListener {
                                Log.d(Log.DEBUG.toString(), "Added $theft")
                                database.collection("thefts").document(auth.currentUser!!.uid)
                                    .get()
                                    .addOnSuccessListener {documents ->
                                    Log.d(Log.INFO.toString(), documents.)
                                }
                            }
                            .addOnFailureListener{e -> Log.d(Log.ERROR.toString(), "$e")}

                    } else {
                        null
                    }
                }.addOnFailureListener {
                    Log.d(Log.ERROR.toString(), "IT NO WORKY WORK")
                }
        }

    }
}
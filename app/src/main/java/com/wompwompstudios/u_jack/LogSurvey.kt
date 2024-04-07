package com.wompwompstudios.u_jack

import android.content.Intent
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
            database.collection("thefts").document(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    val initial: Float = documents.data!![store].toString().toFloat()
                    database.collection("thefts").document(auth.currentUser!!.uid).update(store, (initial - money))
                    finish()
                    Log.d(Log.INFO.toString(), documents.data.toString())
                }.addOnFailureListener {
                    Log.d(Log.ERROR.toString(), "IT NO WORKY WORK")
                }
        }

    }
}
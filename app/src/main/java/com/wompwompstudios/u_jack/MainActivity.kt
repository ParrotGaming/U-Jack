package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null) {
            startActivity(Intent(this,LogInActivity::class.java))
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
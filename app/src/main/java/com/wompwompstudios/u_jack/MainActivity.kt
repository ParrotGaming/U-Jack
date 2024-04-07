package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.wompwompstudios.u_jack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null) {
            startActivity(Intent(this,LogInActivity::class.java))
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(SearchFragment())

        binding.bnvNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.iSearchBtn -> replaceFragment(SearchFragment())
                R.id.iReportBtn -> replaceFragment(ReportFragment())
                R.id.iLogbookBtn -> replaceFragment(LogbookFragment())
                R.id.iAccountBtn -> replaceFragment(AccountFragment())
                else -> {}
            }
            true
        }

        //View references
        val btnSubmitReport = findViewById<Button>(R.id.btnSubmitReport)
        val etUserDescription = findViewById<EditText>(R.id.UserCarDescriptionInput)
        val etEstimatedValue = findViewById<EditText>(R.id.EstimatedCarValueInput)
        val etDifficultyRating = findViewById<EditText>(R.id.DifficultyRatingStatementInput)
    }

    public fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flPageFrame, fragment)
        fragmentTransaction.commit()
    }
}
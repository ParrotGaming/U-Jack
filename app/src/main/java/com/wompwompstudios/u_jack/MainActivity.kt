package com.wompwompstudios.u_jack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
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
        replaceFragment(R.id.flMainPageFrame, SearchFragment())

        binding.bnvNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.iSearchBtn -> replaceFragment(R.id.flMainPageFrame, SearchFragment())
                R.id.iReportBtn -> replaceFragment(R.id.flMainPageFrame, ReportFragment())
                R.id.iLogbookBtn -> replaceFragment(R.id.flMainPageFrame, LogbookFragment())
                R.id.iAccountBtn -> replaceFragment(R.id.flMainPageFrame, AccountFragment())
                else -> {}
            }
            true
        }
    }

    public fun replaceFragment(id: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment)
        fragmentTransaction.commit()
    }
}
package com.parthdesai1208.myar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parthdesai1208.myar.databinding.ActivityMainBinding
import com.parthdesai1208.myar.databinding.ActivitySecondScreenBinding

class SecondScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.btnSignOut.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSignOut.isEnabled = false
            Firebase.auth.signOut()
        }

        Firebase.auth.addAuthStateListener {
            it.currentUser?.let {

            } ?: run {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }

}
package com.parthdesai1208.myar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            AppPref.UserTokenId = ""
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
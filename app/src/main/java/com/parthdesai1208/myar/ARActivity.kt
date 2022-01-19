package com.parthdesai1208.myar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parthdesai1208.myar.databinding.ActivityAractivityBinding
import com.parthdesai1208.myar.databinding.ActivityMainBinding

class ARActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAractivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
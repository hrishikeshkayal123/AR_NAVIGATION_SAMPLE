package com.parthdesai1208.myar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.PermissionChecker
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.ArCoreApk
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parthdesai1208.myar.Helper.CameraPermissionHelper
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

        binding.btnExploreAR.setOnClickListener {
            val availability = ArCoreApk.getInstance().checkAvailability(this)
            if(availability.isSupported){
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this)
                    return@setOnClickListener
                }else{
                    startActivity(Intent(this,ARActivity::class.java))
                }
            }else{
                Snackbar.make(binding.root, "The device is unsupported", Snackbar.LENGTH_LONG).show()
            }
        }

        Firebase.auth.addAuthStateListener {
            it.currentUser?.let {

            } ?: run {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Snackbar.make(binding.root,"Camera permission is needed to explore AR space",Snackbar.LENGTH_LONG).show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            return
        }else{
            startActivity(Intent(this,ARActivity::class.java))
        }
    }

}
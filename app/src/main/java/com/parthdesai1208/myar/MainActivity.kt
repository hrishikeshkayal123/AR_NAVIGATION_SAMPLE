package com.parthdesai1208.myar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parthdesai1208.myar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSignIn.visibility = View.GONE

        auth = Firebase.auth
        auth.addAuthStateListener {
            it.currentUser?.let {
                startSecondActivity()
                binding.progressBar.visibility = View.GONE
            }?: kotlin.run {
                binding.progressBar.visibility = View.GONE
                binding.btnSignIn.visibility = View.VISIBLE
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_oauth_client_id))
                    .requestEmail()
                    .build()

                googleSignInClient = GoogleSignIn.getClient(this, gso)
            }
        }

        binding.btnSignIn.setOnClickListener {

            if(this::googleSignInClient.isInitialized)
            startActivityForResult.launch(googleSignInClient.signInIntent)

        }
    }

    private fun startSecondActivity(){
        startActivity(Intent(this,SecondScreen::class.java))
        finish()
    }

    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.let { it1 ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(it1)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(account.idToken.toString(), null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            //startSecondActivity()
                        } else {
                            Log.e("error","something went wrong")
                        }
                    }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e("error",e.localizedMessage.toString())
            }
        }
    }

}
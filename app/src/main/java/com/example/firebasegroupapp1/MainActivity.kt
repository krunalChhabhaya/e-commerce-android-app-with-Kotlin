package com.example.firebasegroupapp1

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.example.firebasegroupapp1.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseUiUserCollisionException
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            createSignInIntent()
        } else {
            loadUIDesign(currentUser)
        }
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
        this.onSignInResult(result)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let { loadUIDesign(it) }
        } else {
            createSignInIntent()
        }
    }

    private fun loadUIDesign(user: FirebaseUser) {
        setContentView(R.layout.activity_main)

        val fullName = user.displayName

        val fullNameTextView = findViewById<TextView>(R.id.fullNameTextView)
        fullNameTextView.text = fullName

        val shopButton: Button = findViewById(R.id.shop_button)
        shopButton.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }
    }
}
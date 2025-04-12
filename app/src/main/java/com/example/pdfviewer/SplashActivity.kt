package com.example.pdfviewer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pdfviewer.local.UserDao
import com.example.pdfviewer.local.UserDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAuth = FirebaseAuth.getInstance()

        userDao = UserDatabase.getDatabase(this).userDao()
        checkUserLoginStatus()

    }

    private fun checkUserLoginStatus() {
        lifecycleScope.launch {
            val user = userDao.getUserByUid(firebaseAuth.currentUser?.uid ?: "")

            if (user != null) {
                navigateToMainActivity()
            } else {
                navigateToLoginActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
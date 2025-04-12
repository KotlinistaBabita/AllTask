package com.example.pdfviewer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pdfviewer.local.User
import com.example.pdfviewer.local.UserDao
import com.example.pdfviewer.local.UserDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private val RC_SIGN_IN = 100
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        firebaseAuth = FirebaseAuth.getInstance()
        userDao = UserDatabase.getDatabase(this).userDao()


        lifecycleScope.launch {
            val user = userDao.getUserByUid(firebaseAuth.currentUser?.uid ?: "")
            if (user != null) {
                uploadTokenAndRedirect(user.uid)
                return@launch
            }
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_Id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
            signInWithGoogle()
        }


    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val idToken = account?.idToken
                Log.d("LoginActivity", "Google ID Token: $idToken")
                if (idToken != null) {
                    authenticateWithFirebase(idToken, account)
                } else {
                    Toast.makeText(this, "Failed to get ID token", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("LoginActivity", "Google sign-in failed", task.exception)
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateWithFirebase(idToken: String, account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid
                    Log.d("LoginActivity", "Firebase Auth Success - UserId: $userId")

                    lifecycleScope.launch {
                        saveUserToRoom(account, userId!!)
                    }
                } else {
                    Toast.makeText(this, "Firebase Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private suspend fun saveUserToRoom(account: GoogleSignInAccount, userId: String) {
        val user = User(
            uid = userId,
            email = account.email ?: "",
            fcmToken = null
        )

        userDao.insert(user)
    }

    private fun uploadTokenAndRedirect(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Device token: $token")
                val oauthToken = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.result?.token

                if (oauthToken != null) {
                    Log.d("FCM", "OAuth Token: $oauthToken")

                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(userId)
                        .set(
                            mapOf("fcmToken" to token),
                            SetOptions.merge()
                        )
                        .addOnSuccessListener {
                            Log.d("FCM", "Token saved successfully")

                            lifecycleScope.launch {
                                val user = userDao.getUserByUid(userId)
                                user?.let {
                                    it.fcmToken = token
                                    userDao.insert(it)
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("FCM", "Failed to save token: ${it.message}")
                        }
                }
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }


}
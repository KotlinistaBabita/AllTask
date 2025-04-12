package com.example.pdfviewer

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pdfviewer.fragment.HomeFragment
import com.example.pdfviewer.fragment.ImageFragment
import com.example.pdfviewer.fragment.PdfFragment
import com.example.pdfviewer.fragment.SettingsFragment
import com.example.pdfviewer.utils.NotificationPrefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking {
            val isDark = NotificationPrefs.isDarkModeEnabled(this@MainActivity).first()
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        firestore.firestoreSettings = settings
        FirebaseMessaging.getInstance().subscribeToTopic("Test")
        bottomNav = findViewById(R.id.bottom_nav)
        loadFragment(HomeFragment())



        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_pdf -> loadFragment(PdfFragment())
                R.id.nav_image -> loadFragment(ImageFragment())
                R.id.nav_settings -> loadFragment(SettingsFragment())
            }
            true
        }

        val policy= StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
package com.example.pdfviewer.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.pdfviewer.R
import com.example.pdfviewer.utils.NotificationPrefs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private lateinit var switchNotifications: SwitchCompat
    private lateinit var switchTheme: SwitchCompat

    private val notificationPermissionRequestCode = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = requireContext()

        switchNotifications = view.findViewById(R.id.switch_notifications)
        switchTheme = view.findViewById(R.id.switch_theme)

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkNotificationPermissionAndEnable(context)
            } else {
                lifecycleScope.launch {
                    NotificationPrefs.setNotificationsEnabled(context, false)
                }
            }
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                NotificationPrefs.setDarkModeEnabled(context, isChecked)
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }


    private fun checkNotificationPermissionAndEnable(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                lifecycleScope.launch {
                    NotificationPrefs.setNotificationsEnabled(context, true)
                }
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    notificationPermissionRequestCode
                )
            }
        } else {
            lifecycleScope.launch {
                NotificationPrefs.setNotificationsEnabled(context, true)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Notification permission granted", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    NotificationPrefs.setNotificationsEnabled(requireContext(), true)
                }
            } else {
                Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    NotificationPrefs.setNotificationsEnabled(requireContext(), false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val context = requireContext()

        lifecycleScope.launch {
            val isNotificationEnabled = NotificationPrefs.isNotificationEnabled(context).first()
            if (switchNotifications.isChecked != isNotificationEnabled) {
                switchNotifications.setOnCheckedChangeListener(null)
                switchNotifications.isChecked = isNotificationEnabled
                switchNotifications.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        checkNotificationPermissionAndEnable(context)
                    } else {
                        lifecycleScope.launch {
                            NotificationPrefs.setNotificationsEnabled(context, false)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            val isDarkModeEnabled = NotificationPrefs.isDarkModeEnabled(context).first()
            if (switchTheme.isChecked != isDarkModeEnabled) {
                switchTheme.setOnCheckedChangeListener(null)
                switchTheme.isChecked = isDarkModeEnabled
                switchTheme.setOnCheckedChangeListener { _, isChecked ->
                    lifecycleScope.launch {
                        NotificationPrefs.setDarkModeEnabled(context, isChecked)
                        AppCompatDelegate.setDefaultNightMode(
                            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                }
            }
        }
    }


}
package com.example.pdfviewer.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(fragment: Fragment, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(fragment.requireActivity(), permissions, requestCode)
    }
}
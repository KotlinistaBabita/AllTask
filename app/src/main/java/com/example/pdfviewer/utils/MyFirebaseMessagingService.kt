package com.example.pdfviewer.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pdfviewer.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message received: ${remoteMessage.data}")


        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Data payload received: ${remoteMessage.data}")
            val title = remoteMessage.data["title"] ?: "Default Title"
            val body = remoteMessage.data["body"] ?: "Default Message Body"
            showNotification(title, body)
        }
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(title: String?, message: String?) {
        Log.d("FCM", "Show Notification called: Title: $title, Message: $message")

        val builder = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(message ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel", "Default Channel", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, builder.build())
    }
}

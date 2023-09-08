package org.hyperskill.stopwatch

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class NotificationService(val context: Context) {
    val CHANNEL_ID = "org.hyperskill"
    val name = "Notification channel"
    val descriptionText = "Notification channel"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val priority = NotificationCompat.PRIORITY_HIGH // for compatibility with Android below v.8
    val notificationId = 393939
    var notificationManager: NotificationManager? = null
    init {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            notificationManager =
                getSystemService(context, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun sendNotification(icon: Int, title: String, text: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority) // for compatibility with Android below v.8
            .setAutoCancel(true)
            .build()

        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE or Notification.FLAG_INSISTENT

        notificationManager?.notify(notificationId, notification)
    }
}
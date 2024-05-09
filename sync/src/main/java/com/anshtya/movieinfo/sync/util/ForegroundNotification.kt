package com.anshtya.movieinfo.sync.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.anshtya.movieinfo.sync.R

const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "Sync Notification Channel"

fun Context.workNotification(): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SYNC_NOTIFICATION_CHANNEL_ID,
            getString(R.string.work_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)
    }

    return NotificationCompat.Builder(
        this,
        SYNC_NOTIFICATION_CHANNEL_ID,
    )
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(getString(R.string.work_notification_title))
        .setContentText(getString(R.string.work_notification_text))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}
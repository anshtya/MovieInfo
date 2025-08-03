package com.anshtya.movieinfo.data.workmanager.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.anshtya.movieinfo.data.R

const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "Sync Notification Channel"

fun Context.workNotification(): Notification {
    val channel = NotificationChannel(
        SYNC_NOTIFICATION_CHANNEL_ID,
        getString(R.string.work_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    )

    val notificationManager: NotificationManager? =
        getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    notificationManager?.createNotificationChannel(channel)

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
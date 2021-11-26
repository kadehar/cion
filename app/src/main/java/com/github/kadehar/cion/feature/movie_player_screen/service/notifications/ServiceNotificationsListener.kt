package com.github.kadehar.cion.feature.movie_player_screen.service.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.github.kadehar.cion.R
import com.github.kadehar.cion.feature.movie_player_screen.service.PlayerService
import com.google.android.exoplayer2.ui.PlayerNotificationManager

@SuppressLint("RemoteViewLayout")
class ServiceNotificationsListener(
    private val playerService: PlayerService
) : PlayerNotificationManager.NotificationListener {
    private val manager =
        playerService.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        playerService.apply {
            stopForeground(true)
            stopSelf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        val notificationBuilder = NotificationCompat.Builder(
            playerService.applicationContext,
            PlayerService.NOTIFICATION_CHANNEL_ID
        )

        notificationBuilder.apply {
            setSmallIcon(R.drawable.ic_play_movie)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setCategory(NotificationCompat.CATEGORY_SERVICE)
            setPublicVersion(notification)
            setAutoCancel(true)
            setOngoing(true)
        }

        playerService.apply {
            if (Build.VERSION.SDK_INT > 26) {
                manager.createNotificationChannel(
                    NotificationChannel(
                        PlayerService.NOTIFICATION_CHANNEL_ID,
                        PlayerService.NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
                notificationBuilder.setChannelId(PlayerService.NOTIFICATION_CHANNEL_ID)
                val mNotification = notificationBuilder.build()
                startForeground(PlayerService.NOTIFICATION_ID, mNotification)
                manager.notify(PlayerService.NOTIFICATION_ID, mNotification)
            }
            startForeground(PlayerService.NOTIFICATION_ID, notification)
            manager.notify(PlayerService.NOTIFICATION_ID, notification)
        }
    }
}

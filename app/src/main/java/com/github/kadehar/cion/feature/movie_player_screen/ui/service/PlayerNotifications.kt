package com.github.kadehar.cion.feature.movie_player_screen.ui.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.github.kadehar.cion.R
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class PlayerNotifications(private val service: Service) :
    PlayerNotificationManager.NotificationListener {

    @SuppressLint("RemoteViewLayout")
    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        val manager = service.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(
            service.applicationContext,
            "PlayerVideoServiceChannel"
        )
        val remoteView = RemoteViews(service.packageName, R.layout.fragment_movie_player)
        notificationBuilder
            .setContent(remoteView)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)

        if (Build.VERSION.SDK_INT > 26) {
            manager.createNotificationChannel(
                NotificationChannel(
                    "PlayerVideoServiceChannel",
                    "CiOn",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
            notificationBuilder.setChannelId("PlayerVideoServiceChannel")
        }
        val mNotification = notificationBuilder.build()
        service.startForeground(notificationId, mNotification)
        manager.notify(PlayerVideoService.NOTIFICATION_ID, mNotification)
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        service.stopSelf()
    }
}
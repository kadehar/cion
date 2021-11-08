package com.github.kadehar.cion.feature.movie_player_screen.service.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.kadehar.cion.R
import com.github.kadehar.cion.feature.movie_player_screen.service.PlayerService
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class ServiceNotificationManager(
    private val context: Context,
    notificationListener: PlayerNotificationManager.NotificationListener
) {
    private val notificationManager: PlayerNotificationManager

    init {
        notificationManager = PlayerNotificationManager.Builder(
            context,
            PlayerService.NOTIFICATION_ID,
            PlayerService.NOTIFICATION_CHANNEL_ID
        ).apply {
            setChannelNameResourceId(R.string.notification_channel_name)
            setChannelDescriptionResourceId(R.string.notification_channel_description)
            setSmallIconResourceId(R.drawable.ic_play_movie)
            setMediaDescriptionAdapter(DescriptionsAdapter())
            setNotificationListener(notificationListener)
        }.build()
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionsAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return context.getString(R.string.notification_channel_name)
        }

        @RequiresApi(Build.VERSION_CODES.S)
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return PendingIntent.getService(
                context,
                0,
                Intent(context, PlayerService::class.java).apply {
                    putExtra(PlayerService.PLAY_PAUSE_ACTION, 0)
                },
                PendingIntent.FLAG_MUTABLE
            )
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return context.getString(R.string.notification_channel_description)
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }
    }
}
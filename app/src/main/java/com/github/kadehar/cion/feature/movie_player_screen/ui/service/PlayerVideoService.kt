package com.github.kadehar.cion.feature.movie_player_screen.ui.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.github.kadehar.cion.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import org.koin.android.ext.android.inject

class PlayerVideoService : Service() {
    companion object {
        const val VIDEO_FILE = "video"
        const val PLAY_PAUSE_ACTION = "playPauseAction"
        const val NOTIFICATION_ID = 0
    }

    private val exoPlayer by inject<ExoPlayer>()

    inner class PlayerVideoServiceBinder : Binder() {
        fun getPlayerInstance() = exoPlayer
    }

    override fun onBind(intent: Intent?): IBinder {
        intent?.let {
            exoPlayer.playWhenReady = true
            exoPlayer.setMediaItem(MediaItem.fromUri(it.getStringExtra(VIDEO_FILE) ?: ""))
            exoPlayer.prepare()
            displayNotification()
        }
        return PlayerVideoServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.getIntExtra(PLAY_PAUSE_ACTION, -1)) {
                0 -> exoPlayer.playWhenReady = false
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("RemoteViewLayout", "UnspecifiedImmutableFlag", "LaunchActivityFromNotification")
    private fun displayNotification() {
        val remoteView = RemoteViews(packageName, R.layout.fragment_movie_player)

        val intent =
            PendingIntent.getService(this, 0,
                Intent(this, PlayerVideoService::class.java).apply {
                    putExtra(PLAY_PAUSE_ACTION, 0)
                }, 0
            )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, "Default")
        notificationBuilder
            .setContent(remoteView)
            .setContentIntent(intent)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)

        if (Build.VERSION.SDK_INT > 26) {
            manager.createNotificationChannel(NotificationChannel("ID", "Main", NotificationManager.IMPORTANCE_DEFAULT))
            notificationBuilder.setChannelId("ID")
        }
        val notification = notificationBuilder.build()
        startForeground(0, notification)
        manager.notify(NOTIFICATION_ID, notification)
    }
}
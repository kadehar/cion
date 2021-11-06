package com.github.kadehar.cion.feature.movie_player_screen.ui.service.adapter

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import com.github.kadehar.cion.feature.movie_player_screen.ui.service.PlayerVideoService
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class DescriptionAdapter(private val context: Context) :
    PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.mediaMetadata.title ?: "Robot"
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return player.mediaMetadata.albumTitle
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, android.R.drawable.sym_def_app_icon)
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return PendingIntent.getService(
            context,
            0,
            Intent(context, PlayerVideoService::class.java).apply {
                putExtra(PlayerVideoService.PLAY_PAUSE_ACTION, 0)
            },
            0
        )
    }
}
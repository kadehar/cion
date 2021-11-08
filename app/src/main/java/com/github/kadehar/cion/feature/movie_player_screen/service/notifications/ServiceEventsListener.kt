package com.github.kadehar.cion.feature.movie_player_screen.service.notifications

import android.widget.Toast
import com.github.kadehar.cion.feature.movie_player_screen.service.PlayerService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class ServiceEventsListener(
    private val playerService: PlayerService
) : Player.Listener {
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (reason == Player.STATE_READY && !playWhenReady) {
            playerService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(playerService, "An unknown error occurred", Toast.LENGTH_LONG).show()
    }
}
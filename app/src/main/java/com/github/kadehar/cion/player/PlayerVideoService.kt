package com.github.kadehar.cion.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.github.kadehar.cion.base.constants.Constants.MEDIA_FILE
import com.github.kadehar.cion.base.constants.Constants.MEDIA_ROOT_ID
import com.github.kadehar.cion.base.constants.Constants.NETWORK_ERROR
import com.github.kadehar.cion.base.constants.Constants.PLAYER_SERVICE_TAG
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.github.kadehar.cion.player.listeners.PlayerListener
import com.github.kadehar.cion.player.listeners.PlayerNotificationListener
import com.github.kadehar.cion.player.listeners.PlayerPlaybackPreparer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class PlayerVideoService : MediaBrowserServiceCompat() {
    companion object {
        var currentVideoDuration = 0L
            private set
    }

    val exoPlayer by inject<ExoPlayer>()
    private val videoSource by inject<PlayerMediaSource>()

    lateinit var playerNotificationManager: PlayerNotificationManager

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private var currentPlayingVideo: MediaMetadataCompat? = null
    private lateinit var playerListener: PlayerListener
    private var isPlayerInitialized = false

    override fun onCreate() {
        super.onCreate()

        serviceScope.launch {
            videoSource.fetchMediaData()
        }

        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let { intent ->
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        mediaSession = MediaSessionCompat(this, PLAYER_SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        playerNotificationManager = PlayerNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener(this)
        ) {
            currentVideoDuration = exoPlayer.duration
        }

        val videoPlaybackPreparer = PlayerPlaybackPreparer(videoSource) {
            currentPlayingVideo = it
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlaybackPreparer(videoPlaybackPreparer)
            setPlayer(exoPlayer)
        }

        playerListener = PlayerListener(this)
        exoPlayer.addListener(playerListener)
        playerNotificationManager.showNotification(exoPlayer)
    }

    override fun onBind(intent: Intent?): IBinder {
        intent?.getParcelableExtra<Movie>(MEDIA_FILE)?.let { movie ->
            preparePlayer(movie, true)
        }
        return PlayerServiceBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        stopPlayer()
        return super.onUnbind(intent)
    }

    private fun preparePlayer(movie: Movie, playNow: Boolean) {
        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(movie.video))
            prepare()
            playWhenReady = playNow
        }
    }

    private fun stopPlayer() {
        exoPlayer.run {
            playWhenReady = false
            removeListener(playerListener)
            release()
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultSent = videoSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(videoSource.asMediaItems())
                        if (!isPlayerInitialized && videoSource.movies.isNotEmpty()) {
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if (!resultSent) {
                    result.detach()
                }
            }
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService() = this@PlayerVideoService
    }
}
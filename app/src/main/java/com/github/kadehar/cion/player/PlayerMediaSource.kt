package com.github.kadehar.cion.player

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.github.kadehar.cion.feature.movies_screen.data.api.MoviesRepository
import com.github.kadehar.cion.player.PlayerMediaSourceState.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerMediaSource(private val moviesRepository: MoviesRepository) {
    var movies = emptyList<MediaMetadataCompat>()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()
    private var state: PlayerMediaSourceState = Created
        set(value) {
            if (value == Initialized || value == Error) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == Initialized)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean =
        if (state == Created || state == Initializing) {
            onReadyListeners += action
            false
        } else {
            action(state == Initialized)
            true
        }

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = Initializing
        val allMovies = moviesRepository.fetchMovies()
        movies = allMovies.map { movie ->
            Builder()
                .putString(METADATA_KEY_TITLE, movie.title)
                .putString(METADATA_KEY_DISPLAY_TITLE, movie.title)
                .putString(METADATA_KEY_MEDIA_URI, movie.video)
                .putString(METADATA_KEY_MEDIA_ID, movie.id.toString())
                .build()
        }
        state = Initialized
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        movies.forEach { movie ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(movie.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = movies.map { movie ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaUri(movie.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(movie.description.title)
            .setMediaId(movie.description.mediaId)
            .build()
        MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
    }.toMutableList()
}
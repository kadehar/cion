package com.github.kadehar.cion.feature.movie_player_screen.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.kadehar.cion.R
import com.github.kadehar.cion.base.constants.Constants.MEDIA_FILE
import com.github.kadehar.cion.base.utils.hideSystemUI
import com.github.kadehar.cion.base.utils.showSystemUI
import com.github.kadehar.cion.databinding.FragmentMoviePlayerBinding
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.github.kadehar.cion.player.PlayerVideoService


class MoviePlayerFragment : Fragment(R.layout.fragment_movie_player) {
    companion object {
        private const val MOVIE_KEY = "movie"
        fun newInstance(movie: Movie) = MoviePlayerFragment().apply {
            arguments = bundleOf(Pair(MOVIE_KEY, movie))
        }
    }

    private val binding: FragmentMoviePlayerBinding
            by viewBinding(FragmentMoviePlayerBinding::bind)

    private val movie: Movie by lazy {
        requireArguments().getParcelable(MOVIE_KEY)!!
    }

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {}

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            when (service) {
                is PlayerVideoService.PlayerServiceBinder -> {
                    binding.videoPlayerView.player = service.getService().exoPlayer
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(context, PlayerVideoService::class.java)
        intent.putExtra(MEDIA_FILE, movie)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(connection)
    }

    override fun onStart() {
        super.onStart()
        hideSystemUI(requireActivity().window, binding.videoPlayerView)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI(requireActivity().window, binding.videoPlayerView)
    }

    override fun onPause() {
        super.onPause()
        showSystemUI(requireActivity().window, binding.videoPlayerView)
    }

    override fun onStop() {
        super.onStop()
        showSystemUI(requireActivity().window, binding.videoPlayerView)
    }
}
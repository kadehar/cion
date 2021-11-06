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
import com.github.kadehar.cion.base.utils.hideSystemUI
import com.github.kadehar.cion.base.utils.showSystemUI
import com.github.kadehar.cion.databinding.FragmentMoviePlayerBinding
import com.github.kadehar.cion.feature.movie_player_screen.ui.service.PlayerVideoService
import com.google.android.exoplayer2.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviePlayerFragment : Fragment(R.layout.fragment_movie_player) {
    companion object {
        private const val URL_KEY = "url"
        fun newInstance(url: String) = MoviePlayerFragment().apply {
            arguments = bundleOf(Pair(URL_KEY, url))
        }
    }

    private val binding: FragmentMoviePlayerBinding
            by viewBinding(FragmentMoviePlayerBinding::bind)

    private val url: String by lazy {
        requireArguments().getString(URL_KEY)!!
    }

    private val viewModel by viewModel<MoviePlayerViewModel>()

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {}

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is PlayerVideoService.PlayerVideoServiceBinder) {
                binding.videoPlayerView.player = service.getPlayerInstance()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(context, PlayerVideoService::class.java)
        intent.putExtra(PlayerVideoService.VIDEO_FILE, url)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
//
//        binding.videoPlayerView.apply {
//            player = viewModel.exoPlayer
//            viewModel.processUiEvent(PlayerUiEvent.OnPlayerStarted(url))
//        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUI(requireActivity().window, binding.videoPlayerView)
        if (Util.SDK_INT >= 24) {
            viewModel.initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI(requireActivity().window, binding.videoPlayerView)
        if ((Util.SDK_INT < 24)) {
            viewModel.initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        showSystemUI(requireActivity().window, binding.videoPlayerView)
        if (Util.SDK_INT < 24) {
            viewModel.releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        showSystemUI(requireActivity().window, binding.videoPlayerView)
        if (Util.SDK_INT >= 24) {
            viewModel.releasePlayer()
        }
    }
}
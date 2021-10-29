package com.github.kadehar.cion.feature.movie_player_screen.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.kadehar.cion.R
import com.github.kadehar.cion.databinding.FragmentMoviePlayerBinding
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            testTextOutput.text = movie.video
        }
    }
}
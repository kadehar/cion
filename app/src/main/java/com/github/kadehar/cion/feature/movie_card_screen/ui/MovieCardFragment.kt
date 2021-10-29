package com.github.kadehar.cion.feature.movie_card_screen.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.kadehar.cion.R
import com.github.kadehar.cion.base.nav.Screens
import com.github.kadehar.cion.base.utils.formatDate
import com.github.kadehar.cion.base.utils.genresToString
import com.github.kadehar.cion.base.utils.loadImage
import com.github.kadehar.cion.databinding.FragmentMovieCardBinding
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.android.inject

class MovieCardFragment : Fragment(R.layout.fragment_movie_card) {
    companion object {
        private const val MOVIE_KEY = "movie"
        fun newInstance(movie: Movie) = MovieCardFragment().apply {
            arguments = bundleOf(Pair(MOVIE_KEY, movie))
        }
    }

    private val binding: FragmentMovieCardBinding by viewBinding(FragmentMovieCardBinding::bind)
    private val movie: Movie by lazy {
        requireArguments().getParcelable(MOVIE_KEY)!!
    }
    private val router by inject<Router>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cardPoster.loadImage(movie.posterPath)
            cardMovieTitle.text = movie.title
            cardMovieVotes.text = movie.voteAverage.toString()
            cardMovieReleaseDate.text = formatDate(movie.releaseDate)
            cardMovieGenres.text = genresToString(movie.genres)
            cardMovieOverview.text = movie.overview
            cardMoviePlayButton.setOnClickListener {
                router.navigateTo(Screens.moviePlayer(movie))
            }
        }
    }
}
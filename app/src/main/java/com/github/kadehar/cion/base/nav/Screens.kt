package com.github.kadehar.cion.base.nav

import com.github.kadehar.cion.feature.about_movie_screen.ui.AboutMovieFragment
import com.github.kadehar.cion.feature.movie_player_screen.ui.MoviePlayerFragment
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.github.kadehar.cion.feature.movies_screen.ui.MoviesListFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun movieListScreen() = FragmentScreen {
        MoviesListFragment.newInstance()
    }

    fun aboutMovie(movie: Movie) = FragmentScreen {
        AboutMovieFragment.newInstance(movie)
    }

    fun moviePlayer(movie: Movie) = FragmentScreen {
        MoviePlayerFragment.newInstance(movie)
    }
}
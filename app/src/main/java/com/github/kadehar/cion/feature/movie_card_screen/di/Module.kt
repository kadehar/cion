package com.github.kadehar.cion.feature.movie_card_screen.di

import com.github.kadehar.cion.feature.movie_card_screen.ui.MovieCardViewModel
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesCardModule = module {
    viewModel<MovieCardViewModel> { (movie: Movie) ->
        MovieCardViewModel(get<Router>(), movie)
    }
}
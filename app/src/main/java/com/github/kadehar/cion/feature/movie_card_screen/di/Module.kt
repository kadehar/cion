package com.github.kadehar.cion.feature.movie_card_screen.di

import com.github.kadehar.cion.feature.movie_card_screen.ui.MovieCardViewModel
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesCardModule = module {
    viewModel<MovieCardViewModel> {
        MovieCardViewModel(get<Router>())
    }
}
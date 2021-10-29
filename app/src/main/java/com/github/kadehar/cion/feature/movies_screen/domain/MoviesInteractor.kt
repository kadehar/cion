package com.github.kadehar.cion.feature.movies_screen.domain

import com.github.kadehar.cion.base.funcs.attempt
import com.github.kadehar.cion.feature.movies_screen.data.api.MoviesRepository

class MoviesInteractor(private val moviesRepository: MoviesRepository) {
    suspend fun fetchMovies() = attempt {
        moviesRepository.fetchMovies()
    }
}
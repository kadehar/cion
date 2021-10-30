package com.github.kadehar.cion.feature.movies_screen.data.api

import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie

interface MoviesRepository {
    suspend fun fetchMovies(): List<Movie>
}
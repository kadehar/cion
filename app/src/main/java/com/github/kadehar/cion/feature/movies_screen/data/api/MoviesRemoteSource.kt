package com.github.kadehar.cion.feature.movies_screen.data.api

import com.github.kadehar.cion.feature.movies_screen.data.api.model.ResultsRemoteModel

class MoviesRemoteSource(private val moviesApi: MoviesApi) {
    suspend fun fetchMovies(): ResultsRemoteModel = moviesApi.fetchMovies()
}
package com.github.kadehar.cion.feature.movies_screen.ui

import com.github.kadehar.cion.base.view_model.Event
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie

data class ViewState(
    val movies: List<Movie>,
    val errorMessage: String?
) {
    val isInErrorState: Boolean = errorMessage != null
    val isLoading: Boolean = isInErrorState || movies.isEmpty()
}

sealed class UiEvent : Event {
    object FetchMovies : UiEvent()
    data class OnPosterClick(val movie: Movie) : UiEvent()
}

sealed class DataEvent : Event {
    data class SuccessMoviesRequest(val movies: List<Movie>) : DataEvent()
    data class ErrorMoviesRequest(val errorMessage: String) : DataEvent()
}
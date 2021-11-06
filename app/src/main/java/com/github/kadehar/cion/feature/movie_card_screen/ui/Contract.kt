package com.github.kadehar.cion.feature.movie_card_screen.ui

import com.github.kadehar.cion.base.view_model.Event
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie

data class MovieCardViewState(
    val movie: Movie?
)

sealed class MovieCardUiEvent : Event {
    data class OnPlayButtonClicked(val movie: Movie) : MovieCardUiEvent()
}
package com.github.kadehar.cion.feature.movie_card_screen.ui

import com.github.kadehar.cion.base.nav.Screens
import com.github.kadehar.cion.base.view_model.BaseViewModel
import com.github.kadehar.cion.base.view_model.Event
import com.github.terrakok.cicerone.Router

class MovieCardViewModel(private val router: Router) :
    BaseViewModel<MovieCardViewState>() {

    override fun initialViewState() = MovieCardViewState(null)

    override suspend fun reduce(
        event: Event,
        previousState: MovieCardViewState
    ): MovieCardViewState? {
        when (event) {
            is MovieCardUiEvent.OnPlayButtonClicked -> {
                router.navigateTo(Screens.moviePlayer(event.movie.video))
            }
        }
        return null
    }
}
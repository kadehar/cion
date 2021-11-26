package com.github.kadehar.cion.feature.movies_screen.ui

import com.github.kadehar.cion.base.nav.Screens
import com.github.kadehar.cion.base.view_model.BaseViewModel
import com.github.kadehar.cion.base.view_model.Event
import com.github.kadehar.cion.feature.movies_screen.domain.MoviesInteractor
import com.github.terrakok.cicerone.Router

class MoviesListViewModel(
    private val moviesInteractor: MoviesInteractor,
    private val router: Router
) : BaseViewModel<ViewState>() {
    init {
        processUiEvent(UiEvent.FetchMovies)
    }

    override fun initialViewState() = ViewState(
        movies = emptyList(),
        errorMessage = null
    )

    override suspend fun reduce(event: Event, previousState: ViewState): ViewState? {
        when (event) {
            is UiEvent.FetchMovies -> {
                moviesInteractor.fetchMovies().fold(
                    onSuccess = { movies ->
                        processDataEvent(DataEvent.SuccessMoviesRequest(movies = movies))
                    },
                    onError = { exception ->
                        processDataEvent(
                            DataEvent.ErrorMoviesRequest(
                                exception.localizedMessage ?: ""
                            )
                        )
                    }
                )
            }
            is UiEvent.OnInfoButtonClicked -> {
                router.navigateTo(Screens.movieCard(event.movie))
            }
            is UiEvent.OnPlayButtonClicked -> {
                router.navigateTo(Screens.moviePlayer(event.movie))
            }
            is DataEvent.SuccessMoviesRequest -> {
                return previousState.copy(
                    movies = event.movies,
                    errorMessage = null
                )
            }
            is DataEvent.ErrorMoviesRequest -> {
                return previousState.copy(
                    errorMessage = event.errorMessage
                )
            }
        }
        return null
    }
}
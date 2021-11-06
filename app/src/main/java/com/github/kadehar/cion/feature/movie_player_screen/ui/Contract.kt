package com.github.kadehar.cion.feature.movie_player_screen.ui

import com.github.kadehar.cion.base.view_model.Event

data class PlayerViewState(val url: String)

sealed class PlayerUiEvent : Event {
    data class OnPlayerStarted(val url: String) : PlayerUiEvent()
}
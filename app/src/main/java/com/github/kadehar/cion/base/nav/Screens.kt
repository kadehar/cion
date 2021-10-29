package com.github.kadehar.cion.base.nav

import com.github.kadehar.cion.feature.movies_screen.ui.MoviesListFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun movieListScreen() = FragmentScreen() {
        MoviesListFragment.newInstance()
    }
}
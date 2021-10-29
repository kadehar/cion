package com.github.kadehar.cion.feature.movies_screen.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val releaseDate: String,
    val genres: List<Genre>,
    val overview: String,
    val video: String
) : Parcelable

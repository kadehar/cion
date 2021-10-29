package com.github.kadehar.cion.feature.movies_screen.data.api.model

import com.google.gson.annotations.SerializedName

data class ResultRemoteModel(
    @SerializedName("id") val id: Int,
    @SerializedName("genres") val genres: List<GenreRemoteModel>,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: String,
    @SerializedName("vote_average") val voteAverage: Double
)

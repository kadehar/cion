package com.github.kadehar.cion.base.utils

import com.github.kadehar.cion.feature.movies_screen.domain.model.Genre
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val formatter = SimpleDateFormat("yyyy", Locale.ROOT)
    return try {
        formatter.format(parser.parse(date) ?: "")
    } catch (e: ParseException) {
        date
    }
}

fun genresToString(genres: List<Genre>): String {
    return genres.joinToString() { genre -> genre.name }
}

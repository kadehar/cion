package com.github.kadehar.cion.feature.movies_screen.data.api

import com.github.kadehar.cion.base.constants.Constants.BASE_MOVIES_PATH
import com.github.kadehar.cion.base.constants.Constants.CACHE_CONTROL_HEADER
import com.github.kadehar.cion.base.constants.Constants.CACHE_CONTROL_NO_CACHE
import com.github.kadehar.cion.feature.movies_screen.data.api.model.ResultsRemoteModel
import retrofit2.http.GET
import retrofit2.http.Headers

interface MoviesApi {
    @GET(BASE_MOVIES_PATH)
    @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE")
    suspend fun fetchMovies(): ResultsRemoteModel
}
package com.github.kadehar.cion.di

import com.github.kadehar.cion.base.constants.Constants.BASE_MOVIES_URL
import com.github.kadehar.cion.base.network.httpCache10Mb
import com.github.kadehar.cion.base.network.okHttp
import com.github.kadehar.cion.feature.movies_screen.data.api.MoviesRepository
import com.github.kadehar.cion.player.PlayerMediaSource
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.android.exoplayer2.ExoPlayer
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<OkHttpClient> {
        okHttp(cache = httpCache10Mb(androidContext()))
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_MOVIES_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

val navModule = module {
    single<Cicerone<Router>> {
        Cicerone.create(Router())
    }

    single<NavigatorHolder> {
        get<Cicerone<Router>>().getNavigatorHolder()
    }

    single<Router> {
        get<Cicerone<Router>>().router
    }
}

val videoPlayerModule = module {
    factory<ExoPlayer> {
        ExoPlayer.Builder(androidApplication()).build()
    }

    factory<PlayerMediaSource> {
        PlayerMediaSource(moviesRepository = get<MoviesRepository>())
    }
}
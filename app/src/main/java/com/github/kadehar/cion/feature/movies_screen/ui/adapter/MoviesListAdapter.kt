package com.github.kadehar.cion.feature.movies_screen.ui.adapter

import com.github.kadehar.cion.base.utils.loadImage
import com.github.kadehar.cion.base.utils.setThrottledClickListener
import com.github.kadehar.cion.databinding.MovieItemBinding
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun movieAdapterDelegate(onItemClick: (movie: Movie) -> Unit) =
    adapterDelegateViewBinding<Movie, Movie, MovieItemBinding>(
        { layoutInflater, parent ->
            MovieItemBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.setThrottledClickListener {
            onItemClick(item)
        }

        bind {
            binding.apply {
                moviePoster.loadImage(item.posterPath)
            }
        }
    }
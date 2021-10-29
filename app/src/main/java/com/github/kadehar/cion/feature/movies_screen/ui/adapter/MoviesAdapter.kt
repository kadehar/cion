package com.github.kadehar.cion.feature.movies_screen.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.kadehar.cion.base.utils.loadImage
import com.github.kadehar.cion.base.utils.setThrottledClickListener
import com.github.kadehar.cion.databinding.MovieItemBinding
import com.github.kadehar.cion.feature.movies_screen.domain.model.Movie
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class MoviesAdapter(onItemClick: (movie: Movie) -> Unit) :
    AsyncListDifferDelegationAdapter<Movie>(MoviesDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(movieAdapterDelegate(onItemClick))
    }

    private fun movieAdapterDelegate(onItemClick: (movie: Movie) -> Unit) =
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

    class MoviesDiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
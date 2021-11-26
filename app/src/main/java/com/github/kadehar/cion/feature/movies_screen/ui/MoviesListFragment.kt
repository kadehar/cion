package com.github.kadehar.cion.feature.movies_screen.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.kadehar.cion.R
import com.github.kadehar.cion.base.utils.setData
import com.github.kadehar.cion.databinding.FragmentMoviesListBinding
import com.github.kadehar.cion.feature.movies_screen.ui.adapter.movieAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {
    companion object {
        fun newInstance() = MoviesListFragment()
    }

    private val binding: FragmentMoviesListBinding by viewBinding(FragmentMoviesListBinding::bind)
    private val viewModel: MoviesListViewModel by viewModel()
    private val moviesAdapter
            by lazy(LazyThreadSafetyMode.NONE) {
                ListDelegationAdapter(
                    movieAdapterDelegate(
                        onInfoButtonClick = { movie ->
                            viewModel.processUiEvent(UiEvent.OnInfoButtonClicked(movie))
                        },
                        onPlayButtonClick = { movie ->
                            viewModel.processUiEvent(UiEvent.OnPlayButtonClicked(movie))
                        }
                    )
                )
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    private fun initAdapter() {
        binding.moviesList.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun render(viewState: ViewState) {
        binding.progressBar.isGone = !viewState.isLoading
        moviesAdapter.setData(viewState.movies)
    }
}
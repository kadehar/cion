package com.github.kadehar.cion.feature.errors_screen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.kadehar.cion.R
import com.github.kadehar.cion.base.utils.setThrottledClickListener
import com.github.kadehar.cion.databinding.FragmentErrorsScreenBinding
import com.github.kadehar.cion.feature.movies_screen.ui.MoviesListViewModel
import com.github.kadehar.cion.feature.movies_screen.ui.UiEvent
import com.github.kadehar.cion.feature.movies_screen.ui.ViewState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ErrorsScreenFragment : Fragment(R.layout.fragment_errors_screen) {
    companion object {
        fun newInstance() = ErrorsScreenFragment()
    }

    private val binding: FragmentErrorsScreenBinding by viewBinding(FragmentErrorsScreenBinding::bind)
    private val viewModel: MoviesListViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    private fun render(viewState: ViewState) {
        with(binding) {
            errorText.text = getString(R.string.error_text)
            retryButton.setThrottledClickListener {
                viewModel.processUiEvent(UiEvent.OnRetryClicked)
            }
        }
    }
}
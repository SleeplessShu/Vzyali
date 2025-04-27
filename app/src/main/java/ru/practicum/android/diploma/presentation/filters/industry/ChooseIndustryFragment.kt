package ru.practicum.android.diploma.presentation.filters.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooseIndustryBinding
import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.presentation.search.UiError

class ChooseIndustryFragment : Fragment() {
    private var _binding: FragmentChooseIndustryBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<ChooseIndustryViewModel>()
    private val adapter = IndustryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            bToSearch.setOnClickListener {
                findNavController().navigateUp()
            }

            searchField.doOnTextChanged { text, _, _, _ ->
                val searchQuery = text.toString()
                clearFieldButton.isVisible = searchQuery.isNotBlank()
                searchImage.isVisible = searchQuery.isBlank()
                viewModel.search(searchQuery)
            }

            clearFieldButton.setOnClickListener {
                searchField.text.clear()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect(::render)
        }
    }

    private fun render(state: ChooseIndustryScreenState) {
        when (state) {
            ChooseIndustryScreenState.Loading -> renderLoading()
            is ChooseIndustryScreenState.Content -> renderContent(state.data)
            is ChooseIndustryScreenState.Error -> renderError(state.type)
        }
    }

    private fun renderLoading() = with(binding) {
        loadingPlaceholder.root.isVisible = true
        noInternetPlaceholder.root.isVisible = false
        serverErrorPlaceholder.root.isVisible = false
        recycler.isVisible = false
    }

    private fun renderContent(content: List<Industry>) = with(binding) {
        loadingPlaceholder.root.isVisible = false
        noInternetPlaceholder.root.isVisible = false
        serverErrorPlaceholder.root.isVisible = false
        recycler.isVisible = true
        adapter.updateItems(content)
    }

    private fun renderError(errorType: UiError) = with(binding) {
        loadingPlaceholder.root.isVisible = false
        noInternetPlaceholder.root.isVisible = errorType == UiError.NoConnection
        serverErrorPlaceholder.root.isVisible = errorType != UiError.NoConnection
        recycler.isVisible = false
    }
}

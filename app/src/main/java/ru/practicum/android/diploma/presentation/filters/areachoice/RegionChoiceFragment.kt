package ru.practicum.android.diploma.presentation.filters.areachoice

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
import ru.practicum.android.diploma.databinding.FragmentRegionChoiceBinding
import ru.practicum.android.diploma.domain.models.AreaFilter

class RegionChoiceFragment : Fragment() {
    private var _binding: FragmentRegionChoiceBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<AreaChoiceViewModel>()
    private var rvRegions: RecyclerView? = null
    private val adapter = AreaAdapter { area ->
        viewModel.setRegion(area)
        viewModel.setCountryIfNull(area)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRegions = binding.rvRegions
        rvRegions?.layoutManager = LinearLayoutManager(requireContext())
        rvRegions?.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.regionScreenState.collect { state ->
                render(state)
            }
        }

        setupEditText()
        viewModel.getRegionAreas()
    }

    private fun setupEditText() {
        binding.searchField.doOnTextChanged { text, _, _, _ ->
            val searchQuery = text.toString().trim()
            binding.clearFieldButton.isVisible = searchQuery.isNotBlank()
            binding.searchImage.isVisible = searchQuery.isBlank()
            viewModel.getRegionsWithQuery(searchQuery)
        }

        binding.clearFieldButton.setOnClickListener {
            binding.searchField.text.clear()
        }
    }

    private fun render(state: ChooseAreaScreenState) {
        when (state) {
            is ChooseAreaScreenState.Content -> renderContent(state.data)
            is ChooseAreaScreenState.Error -> renderError(state.type)
            ChooseAreaScreenState.Loading -> renderLoading()
        }
    }

    private fun renderError(errorType: FiltersUiError) = with(binding) {
        placeholderLoading.root.isVisible = false
        placeholderNoResults.root.isVisible = errorType is FiltersUiError.BadRequest
        placeholderNoConnection.root.isVisible = errorType is FiltersUiError.NoConnection
        placeholderNoChilds.root.isVisible = errorType is FiltersUiError.NoChildRegions
        placeholderServerError.root.isVisible =
            errorType != FiltersUiError.NoConnection && errorType != FiltersUiError.BadRequest &&
                errorType != FiltersUiError.NoChildRegions
        rvRegions.isVisible = false
    }

    private fun renderLoading() = with(binding) {
        placeholderNoResults.root.isVisible = false
        placeholderLoading.root.isVisible = true
        placeholderNoConnection.root.isVisible = false
        placeholderServerError.root.isVisible = false
        placeholderNoChilds.root.isVisible = false
        rvRegions.isVisible = false
    }

    private fun renderContent(content: List<AreaFilter>) = with(binding) {
        placeholderNoResults.root.isVisible = false
        placeholderLoading.root.isVisible = false
        placeholderNoConnection.root.isVisible = false
        placeholderServerError.root.isVisible = false
        placeholderNoChilds.root.isVisible = false
        rvRegions.isVisible = true
        adapter.areasList = content
        adapter.notifyDataSetChanged()
    }
}

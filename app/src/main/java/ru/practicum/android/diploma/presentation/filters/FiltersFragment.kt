package ru.practicum.android.diploma.presentation.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")
    private val filtersViewModel by activityViewModels<FiltersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupBindings()
        observeState()

//        binding.bAccept.setOnClickListener {
//            val args = Bundle().apply { putString("filters", "some_filter_data") }
//            findNavController().navigate(R.id.action_navigation_filters_to_navigation_main, args)
//        }
    }

    private fun setupBindings() {
        with(binding) {

            btnGroup.isVisible = !binding.industryText.text.isNullOrEmpty()

            bToSearch.setOnClickListener {
                findNavController().navigateUp()
            }

            industryFilterOpen.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_filters_to_navigation_choose_industry)
            }

            clearIndustryFilter.setOnClickListener {
                filtersViewModel.clearSelectedIndustry()
                binding.industryFilterOpen.isVisible = true
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                filtersViewModel.filterState.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: FiltersState) = with(binding) {
        industryText.setText(state.industry?.name.orEmpty())
        industryFilterOpen.isVisible = state.industry == null
        clearIndustryFilter.isVisible = state.industry != null
        btnGroup.isVisible == state.hasAny
    }
}

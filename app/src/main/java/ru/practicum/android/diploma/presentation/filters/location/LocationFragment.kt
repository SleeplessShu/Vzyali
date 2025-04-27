package ru.practicum.android.diploma.presentation.filters.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentJobLocationBinding

class LocationFragment : Fragment() {
    private var _binding: FragmentJobLocationBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.locationLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.bRegion.setOnClickListener {
            findNavController().navigate(R.id.action_LocationFragment_to_RegionFragment)
        }

        binding.bCountry.setOnClickListener {
            findNavController().navigate(R.id.action_LocationFragment_to_CountryFragment)
        }

        binding.bRemoveCountry.setOnClickListener {
            viewModel.removeCountry()
        }

        binding.bRemoveRegion.setOnClickListener {
            viewModel.removeRegion()
        }

        binding.bSelect.setOnClickListener {
            viewModel.saveFilter()
            findNavController().navigateUp()
        }

        binding.bBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun render(state: LocationState) {
        when (state) {
            is LocationState.Empty -> renderEmpty()
            is LocationState.Content -> renderContent(state.country, state.region)
        }
    }

    private fun renderEmpty() = with(binding) {
        showDefaultCountry(true)
        showDefaultRegion(true)
        showRegion(false)
        showCountry(false)
        bSelect.isVisible = false
    }

    private fun renderContent(country: String, region: String) = with(binding) {
        if (country.isNotEmpty()) {
            showCountry(true)
            showDefaultCountry(false)
        }
        if (region.isNotEmpty()) {
            showRegion(true)
            showDefaultRegion(false)
        }
        bSelect.isVisible = true
    }

    private fun showDefaultCountry(visibility: Boolean) {
        with(binding) {
            tvCountryEmpty.isVisible = visibility
            bChooseCountry.isVisible = visibility
        }
    }

    private fun showDefaultRegion(visibility: Boolean) {
        with(binding) {
            tvRegionEmpty.isVisible = visibility
            bChooseRegion.isVisible = visibility
        }
    }

    private fun showCountry(visibility: Boolean) {
        with(binding) {
            bRemoveCountry.isVisible = visibility
            tvCountryNameSelected.isVisible = visibility
            tvCountryWhenSelected.isVisible = visibility
        }
    }

    private fun showRegion(visibility: Boolean) {
        with(binding) {
            bRemoveCountry.isVisible = visibility
            tvCountryNameSelected.isVisible = visibility
            tvCountryWhenSelected.isVisible = visibility
        }
    }
}

package ru.practicum.android.diploma.presentation.filters.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentJobLocationBinding
import ru.practicum.android.diploma.domain.models.AreaFilter
import ru.practicum.android.diploma.presentation.filters.FiltersViewModel
import ru.practicum.android.diploma.presentation.filters.location.areachoice.AreaChoiceViewModel

class LocationFragment : Fragment() {
    private var _binding: FragmentJobLocationBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val filtersViewModel by activityViewModels<FiltersViewModel>()
    private val areaChoiceViewModel by sharedViewModel<AreaChoiceViewModel>()

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
        setupLifesycleScopeListeners()

        binding.bRegion.setOnClickListener {
            findNavController().navigate(R.id.action_LocationFragment_to_RegionFragment)
        }

        binding.bChooseRegion.setOnClickListener {
            findNavController().navigate(R.id.action_LocationFragment_to_RegionFragment)
        }

        binding.bCountry.setOnClickListener {
            findNavController().navigate(R.id.action_LocationFragment_to_CountryFragment)
        }

        binding.bChooseCountry.setOnClickListener {
            findNavController().navigate(R.id.action_LocationFragment_to_CountryFragment)
        }

        binding.bRemoveCountry.setOnClickListener {
            areaChoiceViewModel.removeCountry()
        }

        binding.bRemoveRegion.setOnClickListener {
            areaChoiceViewModel.removeRegion()
        }

        binding.bSelect.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.bBack.setOnClickListener {
            findNavController().navigateUp()
            areaChoiceViewModel.removeCountry()
            filtersViewModel.clearSelectedLocation()
        }
    }

    private fun setupLifesycleScopeListeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                areaChoiceViewModel.countryState.collect { country ->
                    renderState(country, areaChoiceViewModel.regionState.value)
                }
            }
            launch {
                areaChoiceViewModel.regionState.collect { region ->
                    renderState(areaChoiceViewModel.countryState.value, region)
                }
            }
        }
    }

    private fun renderState(country: AreaFilter?, region: AreaFilter?) {
        val countryName = country?.name.orEmpty()
        val regionName = region?.name.orEmpty()

        if (country == null && region == null) {
            renderEmpty()
        } else {
            renderContent(countryName, regionName)
        }

        filtersViewModel.setCountry(country?.id?.toIntOrNull() ?: -1, countryName)
        filtersViewModel.setRegion(region?.id?.toIntOrNull() ?: -1, regionName)
    }

    private fun renderEmpty() = with(binding) {
        showDefaultCountry(true)
        showDefaultRegion(true)
        showRegion(false)
        showCountry(false)
        bSelect.isVisible = false
    }

    private fun renderContent(country: String, region: String) {
        if (country.isNotEmpty()) {
            binding.tvCountryNameSelected.text = country
            showDefaultCountry(false)
            showCountry(true)
        }
        if (region.isNotEmpty()) {
            binding.tvRegionNameSelected.text = region
            showDefaultRegion(false)
            showRegion(true)
        }
        binding.bSelect.isVisible = country.isNotEmpty()
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
            bRemoveRegion.isVisible = visibility
            tvRegionNameSelected.isVisible = visibility
            tvRegionWhenSelected.isVisible = visibility
        }
    }
}

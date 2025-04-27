package ru.practicum.android.diploma.presentation.filters.region_choice

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryChoiceBinding
import ru.practicum.android.diploma.databinding.FragmentRegionChoiceBinding
import ru.practicum.android.diploma.presentation.filters.country_choice.CountryChoiceViewModel

class RegionChoiceFragment : Fragment() {
    private var _binding: FragmentRegionChoiceBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<RegionChoiceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }
}

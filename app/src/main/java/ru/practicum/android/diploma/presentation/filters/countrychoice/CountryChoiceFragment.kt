package ru.practicum.android.diploma.presentation.filters.countrychoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountryChoiceBinding

class CountryChoiceFragment : Fragment() {
    private var _binding: FragmentCountryChoiceBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<CountryChoiceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }
}

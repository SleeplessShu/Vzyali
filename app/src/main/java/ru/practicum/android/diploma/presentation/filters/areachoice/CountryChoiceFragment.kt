package ru.practicum.android.diploma.presentation.filters.areachoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountryChoiceBinding
import ru.practicum.android.diploma.domain.models.AreaFilter

class CountryChoiceFragment : Fragment() {
    private var _binding: FragmentCountryChoiceBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<AreaChoiceViewModel>()
    private var rvCountries: RecyclerView? = null

    private val adapter = AreaAdapter { area ->
        viewModel.setCountry(area)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCountries = binding.rvCountries
        rvCountries?.layoutManager = LinearLayoutManager(requireContext())
        rvCountries?.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.countryScreenState.collect { state ->
                render(state)
            }
        }

        viewModel.getCountryAreas()
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
        placeholderNoConnection.root.isVisible = errorType == FiltersUiError.NoConnection
        placeholderServerError.root.isVisible = errorType != FiltersUiError.NoConnection
        rvCountries.isVisible = false
    }

    private fun renderLoading() = with(binding) {
        placeholderLoading.root.isVisible = true
        placeholderNoConnection.root.isVisible = false
        placeholderServerError.root.isVisible = false
        rvCountries.isVisible = false
    }

    private fun renderContent(content: List<AreaFilter>) = with(binding) {
        placeholderLoading.root.visibility = View.GONE
        placeholderNoConnection.root.visibility = View.GONE
        placeholderServerError.root.visibility = View.GONE
        rvCountries.isVisible = true
        adapter.areasList = content
        adapter.notifyDataSetChanged()
    }
}

package ru.practicum.android.diploma.presentation.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.presentation.filters.location.areachoice.AreaChoiceViewModel
import ru.practicum.android.diploma.util.extensions.setupThousandSeparatorFormatter

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")
    private val filtersViewModel by activityViewModel<FiltersViewModel>()
    private val areaChoiceViewModel by sharedViewModel<AreaChoiceViewModel>()
    private var salaryInput: Int? = null

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
        setupSalaryFormatter()
        setupSalaryHintLogic()

    }

    private fun setupSalaryHintLogic() = with(binding) {
        fun updateSalaryHints() {
            val hasText = salaryExpectedInput.text?.isNotBlank() == true
            val hasFocus = salaryExpectedInput.isFocused

            clearSalaryFilter.isVisible = hasText

            tvSalaryHintUpEmpty.isVisible = !hasText && !hasFocus
            tvSalaryHintUpFocused.isVisible = hasFocus
            tvSalaryHintUpActivated.isVisible = hasText && !hasFocus
        }

        salaryExpectedInput.doOnTextChanged { _, _, _, _ ->
            updateSalaryHints()
        }
        salaryExpectedInput.setOnFocusChangeListener { _, _ ->
            updateSalaryHints()
        }
        updateSalaryHints()
    }


    private fun setupSalaryFormatter() = with(binding) {
        salaryExpectedInput.setupThousandSeparatorFormatter { value ->
            salaryInput = value
            value?.let { filtersViewModel.setSalary(it) }
        }
    }

    private fun setupBindings() {
        with(binding) {
            btnGroup.isVisible = !binding.tvIndustrySelected.text.isNullOrEmpty()

            bBack.setOnClickListener {
                findNavController().navigateUp()
            }

            workPlaceField.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_filters_to_navigation_choose_location)
            }

            industryField.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_filters_to_navigation_choose_industry)
            }

            clearIndustryFilter.setOnClickListener {
                filtersViewModel.clearSelectedIndustry()
                binding.industryFilterOpen.isVisible = true
            }

            clearWorkPlaceFilter.setOnClickListener {
                areaChoiceViewModel.removeCountry()
                filtersViewModel.clearSelectedLocation()
                binding.workPlaceFilterOpen.isVisible = true
            }

            clearSalaryFilter.setOnClickListener {
                salaryExpectedInput.text?.clear()
                filtersViewModel.clearSalary()
            }

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                filtersViewModel.setHideWithoutSalary(isChecked)
            }

            applyBtn.setOnClickListener {
                filtersViewModel.saveAll()
                findNavController().popBackStack()
            }

            cancelBtn.setOnClickListener {
                filtersViewModel.clearAll()
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

    private fun renderState(state: UiFiltersState) = with(binding) {
        val locationName = buildString {
            state.location?.countryName?.takeIf { it.isNotBlank() }?.let { append(it) }
            if (!state.location?.regionName.isNullOrBlank()) {
                if (isNotEmpty()) append(", ")
                append(state.location?.regionName)
            }
        }

        val hasIndustry = state.industry != null
        tvIndustryHint.isVisible = !hasIndustry
        tvIndustryHintUp.isVisible = hasIndustry
        tvIndustrySelected.isVisible = hasIndustry
        tvIndustrySelected.text = state.industry?.name.orEmpty()
        industryFilterOpen.isVisible = !hasIndustry
        clearIndustryFilter.isVisible = hasIndustry

        val hasLocation = state.location != null
        tvWorkPlaceHint.isVisible = !hasLocation
        tvWorkPlaceHintUp.isVisible = hasLocation
        tvWorkPlaceSelected.isVisible = hasLocation
        tvWorkPlaceSelected.text = locationName
        workPlaceFilterOpen.isVisible = state.location == null
        clearWorkPlaceFilter.isVisible = hasLocation

        salaryExpectedInput.setText(state.salaryExpectations?.toString().orEmpty())
        salaryExpectedInput.setSelection(salaryExpectedInput.text?.length ?: 0)

        btnGroup.isVisible = state.hasAny
        checkbox.isChecked = state.hideWithoutSalary
    }
}

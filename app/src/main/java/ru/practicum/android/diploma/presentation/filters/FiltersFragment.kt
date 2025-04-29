package ru.practicum.android.diploma.presentation.filters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")
    private val filtersViewModel by activityViewModel<FiltersViewModel>()
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
        editSalary()

    }

    private fun editSalary() {
        binding.salaryExpectedInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                salaryInput = s?.toString()?.toIntOrNull()
                salaryInput?.let { filtersViewModel.setSalary(it) }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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

            salaryExpectedLayout.setEndIconOnClickListener {
                binding.salaryExpectedInput.text?.clear()
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
        industryText.setText(state.industry?.name.orEmpty())
        salaryExpectedInput.setText(state.salaryExpectations?.toString().orEmpty())
        salaryExpectedInput.setSelection(salaryExpectedInput.text?.length ?: 0)
        industryFilterOpen.isVisible = state.industry == null
        clearIndustryFilter.isVisible = state.industry != null
        btnGroup.isVisible = state.hasAny
        checkbox.isChecked = state.hideWithoutSalary
    }
}

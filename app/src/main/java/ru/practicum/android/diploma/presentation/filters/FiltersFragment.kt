package ru.practicum.android.diploma.presentation.filters

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        setOnBackPressedListener()

        setupSalaryFormatter()
        setupSalaryHintLogic()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmExit()
                }
            }
        )
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
            filtersViewModel.setSalary(value)
        }
    }

    private fun setupBindings() {
        with(binding) {
            val savedValue = filtersViewModel.filterState.value.hideWithoutSalary
            btnGroup.isVisible = !binding.tvIndustrySelected.text.isNullOrEmpty()

            checkbox.apply {
                isChecked = savedValue
                jumpDrawablesToCurrentState()
            }

            bBack.setOnClickListener {
                confirmExit()
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
            }

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                filtersViewModel.setHideWithoutSalary(isChecked)
            }

            applyBtn.setOnClickListener {
                filtersViewModel.saveAll()
                findNavController().popBackStack()
            }

            cancelBtn.setOnClickListener {
                filtersViewModel.clearSalary()
                filtersViewModel.clearSelectedIndustry()
                filtersViewModel.clearSelectedLocation()
                filtersViewModel.setHideWithoutSalary(false)
            }
        }
    }

    private fun setOnBackPressedListener() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isKeyboardOpen(requireView())) {
                    toggleKeyboard(binding.root, false)
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    fun isKeyboardOpen(view: View): Boolean {
        val insets = ViewCompat.getRootWindowInsets(view) ?: return false
        return insets.isVisible(WindowInsetsCompat.Type.ime())
    }

    private fun toggleKeyboard(view: View, show: Boolean) {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) {
            view.post {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }

        } else {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun confirmExit() {
        if (!filtersViewModel.hasChanges()) {
            findNavController().navigateUp()
            return
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(R.string.saveConfirmTitle)
            .setPositiveButton(R.string.saveChanges) { _, _ ->
                filtersViewModel.saveAll()
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.discardChanges) { _, _ ->
                filtersViewModel.discardChanges()
                findNavController().navigateUp()
            }
            .setNeutralButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        val positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        positiveBtn.typeface = ResourcesCompat.getFont(requireContext(), R.font.ys_display_medium)

        val negativeBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        negativeBtn.typeface = ResourcesCompat.getFont(requireContext(), R.font.ys_display_medium)

        val neutralBtn = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
        neutralBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        neutralBtn.typeface = ResourcesCompat.getFont(requireContext(), R.font.ys_display_medium)
    }

    private fun renderState(state: UiFiltersState) = with(binding) {
        val hasUnsaved = filtersViewModel.hasChanges()
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

        val currentDigits = salaryExpectedInput.text
            ?.replace("""\D""".toRegex(), "")
            .orEmpty()

        val expectedDigits = state.salaryExpectations
            ?.toString()
            .orEmpty()

        if (currentDigits != expectedDigits) {
            salaryExpectedInput.setText(expectedDigits)
            salaryExpectedInput.setSelection(salaryExpectedInput.text?.length ?: 0)
        }

        applyBtn.isVisible = hasUnsaved
        cancelBtn.isVisible = state.hasAny
        checkbox.isChecked = state.hideWithoutSalary
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

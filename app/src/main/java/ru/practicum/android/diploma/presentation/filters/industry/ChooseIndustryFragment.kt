package ru.practicum.android.diploma.presentation.filters.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooseIndustryBinding
import ru.practicum.android.diploma.domain.models.main.Industry
import ru.practicum.android.diploma.presentation.filters.FiltersViewModel
import ru.practicum.android.diploma.presentation.search.UiError

class ChooseIndustryFragment : Fragment() {
    private var _binding: FragmentChooseIndustryBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")
    private val industryViewModel by viewModel<ChooseIndustryViewModel>()
    private val filtersViewModel by activityViewModel<FiltersViewModel>()
    private val adapter = IndustryAdapter()
    private var draftIndustry: Industry? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()

        with(binding) {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            adapter.onIndustrySelected = { industry ->
                draftIndustry = industry
                binding.applyBtn.isVisible = true
            }

            bBack.setOnClickListener {
                findNavController().navigateUp()
            }

            applyBtn.setOnClickListener {
                draftIndustry?.let { selected ->
                    filtersViewModel.setIndustry(selected)
                }
                findNavController().popBackStack()
            }
            searchField.doOnTextChanged { text, _, _, _ ->
                val searchQuery = text.toString()
                clearFieldButton.isVisible = searchQuery.isNotBlank()
                searchImage.isVisible = searchQuery.isBlank()
                industryViewModel.search(searchQuery)
            }

            clearFieldButton.setOnClickListener {
                searchField.text.clear()
            }
        }

        setOnBackPressedListener()
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
            industryViewModel.state.collect(::render)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            filtersViewModel.selectedIndustry.collect { industry ->
                binding.applyBtn.isVisible = industry != null
            }
        }
    }

    private fun render(state: ChooseIndustryScreenState) {
        when (state) {
            ChooseIndustryScreenState.Loading -> renderLoading()
            is ChooseIndustryScreenState.Content -> {
                if (state.data.isNotEmpty()) {
                    renderContent(state.data)
                } else {
                    renderEmpty()
                }
            }

            is ChooseIndustryScreenState.Error -> renderError(state.type)
        }
    }

    private fun renderLoading() = with(binding) {
        loadingPlaceholder.root.isVisible = true
        emptyPlaceholder.root.isVisible = false
        noInternetPlaceholder.root.isVisible = false
        serverErrorPlaceholder.root.isVisible = false
        recycler.isVisible = false
    }

    private fun renderContent(content: List<Industry>) = with(binding) {
        loadingPlaceholder.root.isVisible = false
        emptyPlaceholder.root.isVisible = false
        noInternetPlaceholder.root.isVisible = false
        serverErrorPlaceholder.root.isVisible = false
        recycler.isVisible = true
        adapter.updateItems(content)
        adapter.checkedIndustry = filtersViewModel.selectedIndustry.value
        adapter.notifyDataSetChanged()
    }

    private fun renderEmpty() = with(binding) {
        loadingPlaceholder.root.isVisible = false
        emptyPlaceholder.root.isVisible = true
        noInternetPlaceholder.root.isVisible = false
        serverErrorPlaceholder.root.isVisible = false
        recycler.isVisible = false
    }

    private fun renderError(errorType: UiError) = with(binding) {
        loadingPlaceholder.root.isVisible = false
        emptyPlaceholder.root.isVisible = false
        noInternetPlaceholder.root.isVisible = errorType == UiError.NoConnection
        serverErrorPlaceholder.root.isVisible = errorType != UiError.NoConnection
        recycler.isVisible = false
    }
}

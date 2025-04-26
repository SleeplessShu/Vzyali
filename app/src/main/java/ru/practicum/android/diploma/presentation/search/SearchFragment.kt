package ru.practicum.android.diploma.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.main.VacancyShort
import ru.practicum.android.diploma.presentation.VacancyAdapter
import ru.practicum.android.diploma.util.DebounceFunc
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")
    private val searchViewModel by viewModel<SearchViewModel>()
    private var debouncedSearch: DebounceFunc<String>? = null
    private var debouncedClick: DebounceFunc<VacancyShort>? = null
    private var searchQuery: String = ""
    private var isDebounceEnabled = true
    private val adapter: VacancyAdapter = VacancyAdapter(
        onItemClickListener = { vacancy ->
            if (isDebounceEnabled) {
                debouncedSearch?.cancel?.invoke()
                isDebounceEnabled = false
                navigateToVacancyScreen(vacancy)
                debouncedClick?.call?.invoke(vacancy)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            debounceFunc()
            editText()
            setupBindings()
            observeSearchState()
            observeToastFlow()
        }

        setOnBackPressedListener()
    }

    private fun observeSearchState() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchState.collect { state ->
                renderState(state)
                Log.d("SearchFragment", "$state")
            }
        }
    }

    private fun observeToastFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.toastFlow.collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun renderState(state: SearchState) {
        when {
            state.isInitialLoading -> binding.stateLayout.show(ViewState.LOADING)
            state.error != null -> binding.stateLayout.show(ViewState.ERROR, state.error)
            state.content.isNullOrEmpty() -> binding.stateLayout.show(ViewState.EMPTY)
            else -> {
                binding.stateLayout.show(ViewState.CONTENT)
            }
        }
        if (state.content == null && state.error == null && state.isLoading == null) showKeyboard()
        binding.refreshLayout.isRefreshing = state.isRefreshing
        adapter.updateVacancies(
            newVacancies = state.content.orEmpty(),
            showHeaderLoading = state.isRefreshing,
            showFooterLoading = state.isNextPageLoading
        )
        binding.vacanciesFoundText.apply {
            text = state.resultText
            visibility = if (state.showResultText) View.VISIBLE else View.GONE
        }
    }

    private fun editText() {
        binding.searchField.doOnTextChanged { text, _, _, _ ->
            searchQuery = text.toString()
            binding.clearFieldButton.isVisible = searchQuery.isNotBlank()
            binding.searchImage.isVisible = searchQuery.isBlank()

            if (searchQuery.isBlank()) {
                debouncedSearch?.cancel?.invoke()
                searchViewModel.clearSearch()
                isDebounceEnabled = false
            } else {
                isDebounceEnabled = true
                debouncedSearch?.call?.invoke(searchQuery)
            }
        }
    }

    private fun debounceFunc() {
        debouncedSearch = debounce(
            delayMillis = SEARCH_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { _ ->
            searchViewModel.searchVacancy(searchQuery)
            toggleKeyboard(binding.searchField, false)
        }
        debouncedClick = debounce(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
        ) {
            isDebounceEnabled = true
        }
    }

    private fun setupBindings() {
        binding.stateLayout.apply {
            setLoadingView(R.layout.placeholder_loading)
            setEmptyView(R.layout.placeholder_search_new)
            setErrorView(UiError.NoConnection::class.java, R.layout.placeholder_no_internet)
            setErrorView(UiError.BadRequest::class.java, R.layout.placeholder_no_vacancies_list)
            setErrorView(UiError.ServerError::class.java, R.layout.placeholder_server_error_search)
        }
        binding.recyclerView.adapter = adapter

        binding.clearFieldButton.setOnClickListener {
            binding.searchField.text.clear()
            binding.searchField.post {
                binding.searchField.requestFocus()
                toggleKeyboard(binding.searchField, true)
            }
        }
        setEditorActionListener()
        setOnScrollListener()
        binding.toFiltersButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_main_to_navigation_filters)
        }
    }

    private fun setOnScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val layoutManager = rv.layoutManager as? LinearLayoutManager ?: return
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val totalItems = adapter.itemCount

                if (lastVisible >= totalItems - THRESHOLD) {
                    searchViewModel.loadNextPage()
                }

                val swipeRefreshLayout = binding.refreshLayout
                swipeRefreshLayout.setOnRefreshListener {
                    searchViewModel.refreshSearch()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun setEditorActionListener() {
        binding.searchField.setOnEditorActionListener { v, actionId, event ->
            val isActionSearch = actionId == EditorInfo.IME_ACTION_SEARCH
            val isActionDone = actionId == EditorInfo.IME_ACTION_DONE
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            if (isActionSearch || isActionDone || isEnterKey) {
                isDebounceEnabled = true
                debouncedSearch?.cancel?.invoke()
                searchViewModel.searchVacancy(searchQuery)
                toggleKeyboard(v, false)
                true
            } else {
                false
            }
        }
    }

    private fun showKeyboard() {
        binding.searchField.post {
            binding.searchField.requestFocus()
            toggleKeyboard(binding.searchField, true)
        }
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

    private fun setOnBackPressedListener() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isKeyboardOpen(requireView())) {
                    toggleKeyboard(binding.searchField, false)
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

    private fun navigateToVacancyScreen(vacancy: VacancyShort) {
        val args = bundleOf("vacancyId" to vacancy.vacancyId)
        findNavController().navigate(R.id.action_navigation_main_to_navigation_vacancy, args)
    }

    override fun onResume() {
        super.onResume()
        if (searchQuery.isBlank()) {
            binding.searchField.requestFocus()
            toggleKeyboard(binding.searchField, true)
        } else {
            debouncedSearch?.cancel?.invoke()
            toggleKeyboard(binding.searchField, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val THRESHOLD = 2
    }
}

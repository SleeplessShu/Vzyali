package ru.practicum.android.diploma.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<SearchViewModel>()

    // TO DO private val adapter: VacancyAdapter = VacancyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var currentPage = 0
    private var maxPages = Int.MAX_VALUE
    private var lastSearch = ""
    private var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            //recyclerView.adapter = VacancyAdapter()

            toFiltersButton.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_main_to_navigation_filters)
            }

            clearFieldButton.setOnClickListener {
                // TO DO clear text
            }




            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.searchState.collect { state ->

                        if (state.content != null) {
                            if (currentPage == 0) {
                                recyclerView.adapter = VacancyAdapter(state.content.toMutableList())
                                recyclerView.adapter?.notifyDataSetChanged()
                                currentPage++
                            } else {
                                state.content.forEach { (recyclerView.adapter as VacancyAdapter).vacancyList.add(it) }
                                recyclerView.adapter?.notifyDataSetChanged()
                            }

                            maxPages = state.pages
                            recyclerView.addOnScrollListener(scrollListener)
                        }


                        isLoading = state.isLoading

                    }
                }
            }


            val searchTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.searchTask(s.toString())
                    lastSearch = s.toString()
                    currentPage = 0
                }

                override fun afterTextChanged(s: Editable?) {}
            }
            binding.searchField.addTextChangedListener(searchTextWatcher)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            val isAtBottom = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5
            val isMorePages = currentPage < maxPages
            if (!isMorePages) {
                currentPage = 0
                maxPages = Int.MAX_VALUE
                recyclerView.removeOnScrollListener(this)
                return
            }
            val isNotLoading = !isLoading

            if (isAtBottom && isNotLoading) {
                viewModel.searchVacancy(lastSearch, currentPage)
                currentPage++
            }
        }
    }


    // TO DO использовать в Адаптере
    @Suppress("unused")
    private fun navigateToVacancyScreen(vacancy: VacancyShort) {
        val args = bundleOf("vacancyId" to vacancy.vacancyId)
        findNavController().navigate(R.id.action_navigation_main_to_navigation_vacancy, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SEARCH_DELAY = 2000L
    }
}

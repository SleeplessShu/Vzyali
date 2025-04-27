package ru.practicum.android.diploma.presentation.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            bToSearch.setOnClickListener {
                findNavController().navigateUp()
            }

            industryFilterOpen.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_filters_to_navigation_choose_industry)
            }
        }

//        binding.bAccept.setOnClickListener {
//            val args = Bundle().apply { putString("filters", "some_filter_data") }
//            findNavController().navigate(R.id.action_navigation_filters_to_navigation_main, args)
//        }
    }
}

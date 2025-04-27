package ru.practicum.android.diploma.presentation.filters.areachoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentRegionChoiceBinding

class RegionChoiceFragment : Fragment() {
    private var _binding: FragmentRegionChoiceBinding? = null
    private val binding get() = _binding ?: error("Binding is not initialized")

    private val viewModel by viewModel<AreaChoiceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }
}

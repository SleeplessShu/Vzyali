package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favorites.FavoriteViewModel
import ru.practicum.android.diploma.presentation.filters.FiltersViewModel
import ru.practicum.android.diploma.presentation.filters.location.areachoice.AreaChoiceViewModel
import ru.practicum.android.diploma.presentation.filters.industry.ChooseIndustryViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel

val appModule = module {
    viewModel { SearchViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { VacancyViewModel(get(), get()) }
    viewModel { ChooseIndustryViewModel(get(), get()) }
    viewModel { AreaChoiceViewModel(get(), get()) }
    viewModel { FiltersViewModel() }
}

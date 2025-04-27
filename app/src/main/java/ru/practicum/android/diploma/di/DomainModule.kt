package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.AreasInteractor
import ru.practicum.android.diploma.domain.api.IndustryInteractor
import ru.practicum.android.diploma.domain.api.InteractorFavoriteVacancies
import ru.practicum.android.diploma.domain.api.SearchVacancyInteractor
import ru.practicum.android.diploma.domain.impl.AreasInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.domain.impl.InteractorFavoriteVacanciesImpl
import ru.practicum.android.diploma.domain.impl.SearchVacancyInteractorImpl

val domainModule = module {
    single<InteractorFavoriteVacancies> {
        InteractorFavoriteVacanciesImpl(get())
    }

    single<SearchVacancyInteractor> {
        SearchVacancyInteractorImpl(get())
    }

    single<IndustryInteractor> {
        IndustryInteractorImpl(get())
    }

    single<AreasInteractor> {
        AreasInteractorImpl(get())
    }
}

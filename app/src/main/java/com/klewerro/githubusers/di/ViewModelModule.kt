package com.klewerro.githubusers.di

import com.klewerro.githubusers.core.presentation.savedState.AndroidSavedStateHandleProvider
import com.klewerro.githubusers.core.presentation.savedState.SavedStateProvider
import com.klewerro.githubusers.userDetails.domain.usecase.GetAndObserveRepositoriesUseCase
import com.klewerro.githubusers.userDetails.domain.usecase.GetAndObserveUserDetailsUseCase
import org.koin.dsl.module

val viewModelModule = module {
    factory<SavedStateProvider> {
        AndroidSavedStateHandleProvider(savedStateHandle = get())
    }
    factory {
        GetAndObserveUserDetailsUseCase(userInformationRepository = get())
    }
    factory {
        GetAndObserveRepositoriesUseCase(userInformationRepository = get())
    }
}

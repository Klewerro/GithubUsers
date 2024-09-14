package com.klewerro.githubusers.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.klewerro.githubusers.users.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val searchText = MutableStateFlow("")
    val state = combine(
        searchText,
        userRepository.searchQueryFlow.map { it.isNotBlank() }
    ) { searchText, isRepositoryQueryNotBlank ->
        UsersState(
            searchText = searchText,
            isRepositoryQueryNotBlank = isRepositoryQueryNotBlank
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10_000), UsersState())

    val searchPager = userRepository.searchPager
        .cachedIn(viewModelScope)

    init {
        loadSavedTextSearch()
    }

    fun onEvent(event: UsersEvent) {
        when (event) {
            is UsersEvent.SearchTextChanged -> searchText.update {
                event.text.trim()
            }

            UsersEvent.Search -> {
                viewModelScope.launch(Dispatchers.IO) {
                    userRepository.updateSearchQuery(state.value.searchText)
                }
            }
        }
    }

    private fun loadSavedTextSearch() {
        viewModelScope.launch {
            val savedText = userRepository.searchQueryFlow.first()
            if (savedText.isNotBlank()) {
                searchText.update {
                    savedText
                }
            }
        }
    }
}

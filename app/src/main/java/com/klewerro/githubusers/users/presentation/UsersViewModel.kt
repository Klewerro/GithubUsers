package com.klewerro.githubusers.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.klewerro.githubusers.users.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("bob") // Todo: Temporary
    val searchText = _searchText.asStateFlow()

    val searchPager = userRepository.searchPager
        .cachedIn(viewModelScope)

    fun onEvent(event: UsersEvent) {
        when (event) {
            is UsersEvent.SearchTextChanged -> _searchText.update { event.text }
            UsersEvent.Search -> {
                userRepository.updateSearchQuery(searchText.value)
            }
        }
    }
}

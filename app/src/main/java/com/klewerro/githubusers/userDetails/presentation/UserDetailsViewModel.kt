package com.klewerro.githubusers.userDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.presentation.navigation.CustomNavType
import com.klewerro.githubusers.core.presentation.navigation.NavRoutes
import com.klewerro.githubusers.userDetails.domain.GithubRepository
import com.klewerro.githubusers.userDetails.domain.GithubRepositoryRepository
import com.klewerro.githubusers.users.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class UserDetailsViewModel(
    savedState: SavedStateHandle,
    private val githubRepositoryRepository: GithubRepositoryRepository
) : ViewModel() {

    private val user = MutableStateFlow(
        savedState.toRoute<NavRoutes.UserDetailsScreen>(
            typeMap = mapOf(typeOf<User>() to CustomNavType.UserType)
        ).user
    )
    private val repositories = MutableStateFlow<List<GithubRepository>>(emptyList())
    val state: StateFlow<UserDetailsState> = combine(
        user,
        repositories
    ) { userValue, repositories ->
        UserDetailsState(
            userValue,
            repositories
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10_000),
        UserDetailsState(
            // I can do that, because value is from NavigationComponent using SavedStateHandle
            user.value
        )
    )

    init {
        githubRepositoryRepository.observeUserRepositories(user.value.id)
            .onEach { repos ->
                repositories.update { repos }
            }
            .launchIn(viewModelScope)
        getRepositories()
    }

    private fun getRepositories() {
        val userLogin = user.value.login
        val userId = user.value.id
        viewModelScope.launch(Dispatchers.IO) {
            try {
                githubRepositoryRepository.getUserRepositories(userId, userLogin)
            } catch (apiException: GithubApiException) {
                apiException.printStackTrace()
                // Todo: implement showing error
            }
        }
    }
}

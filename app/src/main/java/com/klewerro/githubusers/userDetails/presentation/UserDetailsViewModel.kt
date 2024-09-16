package com.klewerro.githubusers.userDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.presentation.navigation.CustomNavType
import com.klewerro.githubusers.core.presentation.navigation.NavRoutes
import com.klewerro.githubusers.userDetails.domain.GithubRepositoryRepository
import com.klewerro.githubusers.users.domain.model.User
import kotlin.reflect.typeOf
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
import timber.log.Timber

class UserDetailsViewModel(
    savedState: SavedStateHandle,
    private val githubRepositoryRepository: GithubRepositoryRepository
) : ViewModel() {

    private val user = MutableStateFlow(
        savedState.toRoute<NavRoutes.UserDetailsScreen>(
            typeMap = mapOf(typeOf<User>() to CustomNavType.UserType)
        ).user
    )

    private val _state = MutableStateFlow(
        UserDetailsState(
            // I can do that, because value is from NavigationComponent using SavedStateHandle
            user = user.value
        )
    )
    val state: StateFlow<UserDetailsState> = combine(
        _state,
        user
    ) { _state, userValue ->
        UserDetailsState(
            user = userValue,
            repositories = _state.repositories,
            apiError = _state.apiError,
            isLoading = _state.isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10_000),
        UserDetailsState(user = user.value)
    )

    init {
        getRepositoriesAndObserve()
    }

    fun onEvent(event: UserDetailsEvent) {
        when (event) {
            UserDetailsEvent.DismissError -> _state.update {
                it.copy(apiError = null)
            }

            UserDetailsEvent.RefreshData -> getRepositories()
        }
    }

    private fun getRepositoriesAndObserve() {
        viewModelScope.launch(Dispatchers.IO) {
            startLoading()
            val userHaveAnyRepository =
                githubRepositoryRepository.userHaveAnyRepository(state.value.user.id)
            if (!userHaveAnyRepository) {
                Timber.i("Local user repos not found, so fetching from API.")
                getRepositories()
            } else {
                Timber.i("Local user repos found, so only observing db")
            }

            githubRepositoryRepository.observeUserRepositories(user.value.id)
                .onEach { repos ->
                    Timber.d("Received ${repos.size} local user repos.")
                    _state.update {
                        it.copy(
                            repositories = repos
                        )
                    }
                }
                .launchIn(this)
            if (userHaveAnyRepository) {
                endLoading()
            }
        }
    }

    private fun getRepositories() {
        startLoading()
        val userLogin = user.value.login
        val userId = user.value.id
        viewModelScope.launch(Dispatchers.IO) {
            try {
                githubRepositoryRepository.getUserRepositories(userId, userLogin)
                endLoading()
            } catch (apiException: GithubApiException) {
                Timber.e(apiException, "Error in getting user repositories.")
                _state.update {
                    it.copy(
                        apiError = apiException,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun startLoading() = _state.update { it.copy(isLoading = true) }

    private fun endLoading() = _state.update { it.copy(isLoading = false) }
}

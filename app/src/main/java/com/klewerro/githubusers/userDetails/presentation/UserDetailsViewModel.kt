package com.klewerro.githubusers.userDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.domain.dispatcher.DispatcherProvider
import com.klewerro.githubusers.core.presentation.savedState.SavedStateProvider
import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
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
    savedState: SavedStateProvider,
    private val userInformationRepository: UserInformationRepository,
    private val dispatches: DispatcherProvider
) : ViewModel() {

    private val user = MutableStateFlow(savedState.getUser())

    private val _state = MutableStateFlow(
        UserDetailsState(
            // I can do that, because value is from NavigationComponent using SavedStateHandle
            user = user.value
        )
    )
    val state: StateFlow<UserDetailsState> = combine(
        _state,
        user
    ) { stateValue, userValue ->
        UserDetailsState(
            user = userValue,
            repositories = stateValue.repositories,
            userDetails = stateValue.userDetails,
            apiError = stateValue.apiError,
            isLoading = stateValue.isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10_000),
        UserDetailsState(user = user.value)
    )

    init {
        getUserDetailsAndObserve()
        getRepositoriesAndObserve()
    }

    fun onEvent(event: UserDetailsEvent) {
        when (event) {
            UserDetailsEvent.DismissError -> _state.update {
                it.copy(apiError = null)
            }

            UserDetailsEvent.RefreshData -> {
                getUserDetails()
                getRepositories()
            }
        }
    }

    private fun getUserDetailsAndObserve() {
        val user = state.value.user
        viewModelScope.launch(dispatches.io) {
            startLoading()
            val alreadyHaveSavedUserDetails =
                userInformationRepository.userHaveSavedUserDetails(user.id)
            if (!alreadyHaveSavedUserDetails) {
                Timber.i("Local user details not found, so fetching from API.")
                userInformationRepository.getUserDetails(user.login)
            } else {
                Timber.i("Local user details found, so only observing db.")
            }

            userInformationRepository.observeUserDetails(user.id)
                .onEach { userDetails ->
                    Timber.d("Received local user details.")
                    _state.update {
                        it.copy(
                            userDetails = userDetails
                        )
                    }
                }
                .launchIn(this)

            if (alreadyHaveSavedUserDetails) {
                endLoading()
            }
        }
    }

    private fun getRepositoriesAndObserve() {
        viewModelScope.launch(dispatches.io) {
            startLoading()
            val userHaveAnyRepository =
                userInformationRepository.userHaveAnyRepository(state.value.user.id)
            if (!userHaveAnyRepository) {
                Timber.i("Local user repos not found, so fetching from API.")
                getRepositories()
            } else {
                Timber.i("Local user repos found, so only observing db.")
            }

            userInformationRepository.observeUserRepositories(user.value.id)
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

    private fun getUserDetails() {
        startLoading()
        val userLogin = user.value.login
        viewModelScope.launch(dispatches.io) {
            try {
                userInformationRepository.getUserDetails(userLogin)
                endLoading()
            } catch (apiException: GithubApiException) {
                Timber.e(apiException, "Error in getting user details.")
                _state.update {
                    it.copy(
                        apiError = apiException,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getRepositories() {
        startLoading()
        val userLogin = user.value.login
        val userId = user.value.id
        viewModelScope.launch(dispatches.io) {
            try {
                userInformationRepository.getUserRepositories(userId, userLogin)
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

package com.klewerro.githubusers.userDetails.presentation

import androidx.lifecycle.viewModelScope
import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.domain.dispatcher.DispatcherProvider
import com.klewerro.githubusers.core.presentation.savedState.SavedStateProvider
import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.userDetails.domain.usecase.GetAndObserveRepositoriesUseCase
import com.klewerro.githubusers.userDetails.domain.usecase.GetAndObserveUserDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import timber.log.Timber

@OptIn(KoinExperimentalAPI::class)
class UserDetailsViewModel(
    savedState: SavedStateProvider,
    private val userInformationRepository: UserInformationRepository,
    private val dispatches: DispatcherProvider,
    private val getAndObserveUserDetailsUseCase: GetAndObserveUserDetailsUseCase,
    private val getAndObserveRepositoriesUseCase: GetAndObserveRepositoriesUseCase
) : ScopeViewModel() {

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
        viewModelScope.launch(dispatches.io) {
            try {
                val user = state.value.user
                startLoading()
                getAndObserveUserDetailsUseCase(user.id, user.login)
                    .onEach { userDetails ->
                        _state.update {
                            it.copy(
                                userDetails = userDetails
                            )
                        }
                    }
                    .catch { throwable ->
                        unexpectedFlowErrorStateUpdate(throwable)
                    }
                    .launchIn(this)
                getAndObserveRepositoriesUseCase(user.id, user.login)
                    .onEach { repos ->
                        Timber.d("Received ${repos.size} local user repos.")
                        _state.update {
                            it.copy(
                                repositories = repos
                            )
                        }
                    }
                    .catch { throwable ->
                        unexpectedFlowErrorStateUpdate(throwable)
                    }
                    .launchIn(this)
                endLoading()
            } catch (apiException: GithubApiException) {
                Timber.e(
                    apiException,
                    "Error in getting and observing user repositories or userDetails."
                )
                _state.update {
                    it.copy(
                        apiError = apiException,
                        isLoading = false
                    )
                }
            }
        }
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

    private fun getUserDetails() {
        startLoading()
        val userLogin = user.value.login
        viewModelScope.launch(dispatches.io) {
            runCatchGithubApiException {
                userInformationRepository.getUserDetails(userLogin)
            }
            endLoading()
        }
    }

    private fun getRepositories() {
        startLoading()
        val userLogin = user.value.login
        val userId = user.value.id
        viewModelScope.launch(dispatches.io) {
            runCatchGithubApiException {
                userInformationRepository.getUserRepositories(userId, userLogin)
            }
            endLoading()
        }
    }

    private fun startLoading() = _state.update { it.copy(isLoading = true) }

    private fun endLoading() = _state.update { it.copy(isLoading = false) }

    private suspend fun runCatchGithubApiException(function: suspend () -> Unit) {
        try {
            function()
        } catch (apiException: GithubApiException) {
            Timber.e(apiException, "Error in getting user information (repos or details.")
            _state.update {
                it.copy(
                    apiError = apiException,
                    isLoading = false
                )
            }
        }
    }

    private fun unexpectedFlowErrorStateUpdate(originalException: Throwable) {
        _state.update {
            it.copy(
                apiError = GithubApiException(
                    errorType = GithubApiErrorType.UNKNOWN_ERROR,
                    originalException = originalException
                ),
                isLoading = false
            )
        }
    }
}

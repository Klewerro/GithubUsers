package com.klewerro.githubusers.userDetails.domain.usecase

import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.userDetails.domain.model.GithubRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetAndObserveRepositoriesUseCase(
    private val userInformationRepository: UserInformationRepository
) {
    var isFlowInitialized = false
    lateinit var flowInternal: Flow<List<GithubRepository>>

    suspend operator fun invoke(userId: Int, userLogin: String): Flow<List<GithubRepository>> {
        val userHaveAnyRepository =
            userInformationRepository.userHaveAnyRepository(userId)
        if (!userHaveAnyRepository) {
            Timber.i("Local user repos not found, so fetching from API.")
            userInformationRepository.getUserRepositories(userId, userLogin)
        } else {
            Timber.i("Local user repos found, so only observing db.")
        }
        return if (isFlowInitialized) {
            flowInternal
        } else {
            flowInternal = userInformationRepository.observeUserRepositories(userId)
            flowInternal
        }
    }
}

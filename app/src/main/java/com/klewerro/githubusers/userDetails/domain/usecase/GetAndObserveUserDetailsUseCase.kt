package com.klewerro.githubusers.userDetails.domain.usecase

import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.userDetails.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetAndObserveUserDetailsUseCase(
    private val userInformationRepository: UserInformationRepository
) {
    var isFlowInitialized = false
    lateinit var flowInternal: Flow<UserDetails>

    suspend operator fun invoke(userId: Int, userLogin: String): Flow<UserDetails> {
        val alreadyHaveSavedUserDetails =
            userInformationRepository.userHaveSavedUserDetails(userId)
        if (!alreadyHaveSavedUserDetails) {
            userInformationRepository.getUserDetails(userLogin)
        } else {
            Timber.i("Local user details found, so only observing db.")
        }
        return if (isFlowInitialized) {
            flowInternal
        } else {
            flowInternal = userInformationRepository.observeUserDetails(userId)
            flowInternal
        }
    }
}

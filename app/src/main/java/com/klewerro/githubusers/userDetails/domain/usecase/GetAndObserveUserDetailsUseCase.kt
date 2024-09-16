package com.klewerro.githubusers.userDetails.domain.usecase

import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.userDetails.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetAndObserveUserDetailsUseCase(
    private val userInformationRepository: UserInformationRepository
) {
    suspend operator fun invoke(userId: Int, userLogin: String): Flow<UserDetails> {
        try {
            val alreadyHaveSavedUserDetails =
                userInformationRepository.userHaveSavedUserDetails(userId)
            if (!alreadyHaveSavedUserDetails) {
                userInformationRepository.getUserDetails(userLogin)
            } else {
                Timber.i("Local user details found, so only observing db.")
            }
        } finally {
            // Possible exceptions fill be thrown anyway inside flow
            return userInformationRepository.observeUserDetails(userId)
        }
    }
}

package com.klewerro.githubusers.fake

import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.util.testData.GithubRepositoryTestData
import com.klewerro.githubusers.core.util.testData.UserDetailsTestData
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.userDetails.domain.model.GithubRepository
import com.klewerro.githubusers.userDetails.domain.model.UserDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeUserInformationRepository(
    var userHaveAnyRepository: Boolean,
    var userHaveAnySavedDetails: Boolean,
    var defaultDelay: Long = 1_000L,
    var getUserRepositoriesReturnSuccess: Boolean = true,
    var savedGithubRepositories: List<GithubRepository> = listOf(
        GithubRepositoryTestData.repository1,
        GithubRepositoryTestData.repository2,
        GithubRepositoryTestData.repository3
    ),
    var user1Repos: List<GithubRepository> = savedGithubRepositories.take(2),
    var user2Repos: List<GithubRepository> = listOf(savedGithubRepositories.last()),

    var getUserDetailsReturnSuccess: Boolean = true,
    var userDetails: UserDetails = UserDetailsTestData.userDetails1
) : UserInformationRepository {

    var wasGetUserRepositoriesCalled: Boolean = false
    var wasGetUserDetailsCalled: Boolean = false

    private val reposFlow = MutableStateFlow<Map<Int, List<GithubRepository>>>(emptyMap())
    private val detailsFlow = MutableStateFlow<Map<String, UserDetails>>(emptyMap())

    override fun observeUserRepositories(userId: Int): Flow<List<GithubRepository>> {
        val repos = when (userId) {
            UserTestData.user1.id -> if (userHaveAnyRepository || reposFlow.value.keys.contains(userId)) user1Repos else emptyList()
            UserTestData.user2.id -> if (userHaveAnyRepository || reposFlow.value.keys.contains(userId)) user2Repos else emptyList()
            else -> emptyList()
        }

        reposFlow.update {
            it.plus(userId to repos)
        }

        return reposFlow.map {
            it[userId]!!
        }
    }

    /**
     * Only triggers refresh in httpClient and returning data in database flow,
     * so only delay in test function.
     */
    override suspend fun getUserRepositories(userId: Int, login: String) {
        wasGetUserRepositoriesCalled = true
        delay(defaultDelay)

        val repos = when (userId) {
            UserTestData.user1.id -> if (getUserRepositoriesReturnSuccess) {
                user1Repos
            } else {
                throw GithubApiException(
                    errorType = GithubApiErrorType.UNKNOWN_ERROR
                )
            }

            UserTestData.user2.id -> if (getUserRepositoriesReturnSuccess) {
                user2Repos
            } else {
                throw GithubApiException(
                    errorType = GithubApiErrorType.UNKNOWN_ERROR
                )
            }

            else -> emptyList()
        }

        reposFlow.update {
            it.plus(userId to repos)
        }
    }

    override suspend fun userHaveAnyRepository(userId: Int): Boolean {
        val result = when (userId) {
            UserTestData.user1.id -> userHaveAnyRepository ?: true
            UserTestData.user2.id -> userHaveAnyRepository ?: true
            else -> userHaveAnyRepository ?: false
        }
        return result
    }

    override fun observeUserDetails(userId: Int): Flow<UserDetails> {
        val details = when (userId) {
            UserTestData.user1.id -> {
                if (userHaveAnySavedDetails || detailsFlow.value.containsKey(userDetails.login)) userDetails else null
            }

            UserTestData.user2.id -> {
                if (userHaveAnySavedDetails) userDetails else null
            }

            else -> null
        }

        details?.let {
            detailsFlow.update {
                it.plus(userDetails.login to details)
            }
        }

        return detailsFlow.map { it[userDetails.login] }.filterNotNull()
    }

    override suspend fun getUserDetails(login: String) {
        wasGetUserDetailsCalled = true
        delay(defaultDelay)

        val details = when (login) {
            UserTestData.user1.login -> if (getUserDetailsReturnSuccess) {
                userDetails
            } else {
                throw GithubApiException(
                    errorType = GithubApiErrorType.UNKNOWN_ERROR
                )
            }

            UserTestData.user2.login -> if (getUserDetailsReturnSuccess) {
                userDetails
            } else {
                throw GithubApiException(
                    errorType = GithubApiErrorType.UNKNOWN_ERROR
                )
            }

            else -> null
        }

        details?.let {
            detailsFlow.update {
                it.plus(login to details)
            }
        }
    }

    override suspend fun userHaveSavedUserDetails(userId: Int): Boolean {
        val result = when (userId) {
            UserTestData.user1.id -> userHaveAnySavedDetails ?: true
            UserTestData.user2.id -> userHaveAnySavedDetails ?: true
            else -> userHaveAnySavedDetails ?: false
        }
        return result
    }
}

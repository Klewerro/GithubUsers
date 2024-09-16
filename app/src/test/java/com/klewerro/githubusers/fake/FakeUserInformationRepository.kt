package com.klewerro.githubusers.fake

import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.util.testData.GithubRepositoryTestData
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.userDetails.domain.GithubRepository
import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeUserInformationRepository(
    var userHaveAnyRepository: Boolean,
    var defaultDelay: Long = 1_000L,
    var getUserRepositoriesReturnSuccess: Boolean = true,
    var savedGithubRepositories: List<GithubRepository> = listOf(
        GithubRepositoryTestData.repository1,
        GithubRepositoryTestData.repository2,
        GithubRepositoryTestData.repository3
    ),
    var user1Repos: List<GithubRepository> = savedGithubRepositories.take(2),
    var user2Repos: List<GithubRepository> = listOf(savedGithubRepositories.last())
) : UserInformationRepository {

    var wasGetUserRepositoriesCalled: Boolean = false

    private val flow = MutableStateFlow<Map<Int, List<GithubRepository>>>(emptyMap())

    override fun observeUserRepositories(userId: Int): Flow<List<GithubRepository>> {
        val repos = when (userId) {
            UserTestData.user1.id -> if (userHaveAnyRepository) user1Repos else emptyList()
            UserTestData.user2.id -> if (userHaveAnyRepository) user2Repos else emptyList()
            else -> emptyList()
        }

        flow.update {
            it.plus(userId to repos)
        }

        return flow.map { it[userId] ?: emptyList() }
    }

    /**
     * Only triggers refresh in httpClient and returning data in database flow,
     * so only delay in test function.
     */
    override suspend fun getUserRepositories(userId: Int, login: String) {
        wasGetUserRepositoriesCalled = true
//        Log.d(TAG, "getUserRepositories(): fetch started")
        delay(defaultDelay)
//        Log.d(TAG, "getUserRepositories(): fetched")

        val repos = when (userId) {
            UserTestData.user1.id -> if (getUserRepositoriesReturnSuccess) user1Repos else throw GithubApiException(errorType = GithubApiErrorType.UNKNOWN_ERROR)
            UserTestData.user2.id -> if (getUserRepositoriesReturnSuccess) user2Repos else throw GithubApiException(errorType = GithubApiErrorType.UNKNOWN_ERROR)
            else -> emptyList()
        }

        flow.update {
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
}

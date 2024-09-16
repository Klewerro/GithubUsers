package com.klewerro.githubusers.userDetails.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.fake.FakeGithubRepositoryRepository
import com.klewerro.githubusers.fake.FakeSavedStateProvider
import com.klewerro.githubusers.util.MainCoroutineExtension
import com.klewerro.githubusers.util.TestDispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class UserDetailsViewModelTest {

    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var fakeSavedState: FakeSavedStateProvider
    private lateinit var fakeGithubRepositoryRepository: FakeGithubRepositoryRepository

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    fun setUp(
        userHaveAnyRepository: Boolean = true,
        getUserRepositoriesReturnSuccess: Boolean = true
    ) {
        fakeSavedState = FakeSavedStateProvider()
        fakeGithubRepositoryRepository = FakeGithubRepositoryRepository(
            userHaveAnyRepository = userHaveAnyRepository,
            getUserRepositoriesReturnSuccess = getUserRepositoriesReturnSuccess
        )
        val testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
        userDetailsViewModel = UserDetailsViewModel(
            fakeSavedState,
            fakeGithubRepositoryRepository,
            testDispatchers
        )
    }

    @Test
    fun `test if user is get from savedState and emitted in state as first emission`() =
        runBlocking {
            setUp()
            userDetailsViewModel.state.test {
                val item1 = awaitItem()

                assertThat(item1).isNotNull()
                assertThat(item1.user).isEqualTo(UserTestData.user1)
            }
        }

    @Test
    fun `init getRepositoriesAndObserve when user haven respositories not call getUserRepositories`() =
        runTest {
            setUp()
            userDetailsViewModel.state.test {
                val item1 = awaitItem()
                val itemLoadedRepos = awaitItem()

                assertThat(item1.repositories).isEmpty()
                assertThat(itemLoadedRepos.repositories).isNotEmpty()
                assertThat(
                    itemLoadedRepos.repositories
                ).isEqualTo(fakeGithubRepositoryRepository.user1Repos)
            }
        }

    @Test
    fun `init getRepositoriesAndObserve when user haven't any repository successfully call getUserRepositories and updates values in the state`() =
        runTest {
            setUp(userHaveAnyRepository = false)
            userDetailsViewModel.state.test {
                val item1 = awaitItem()
                val itemLoading = awaitItem()
                val itemGetRepos = awaitItem()

                assertThat(item1.repositories).isEmpty()
                assertThat(itemLoading.isLoading).isTrue()
                assertThat(itemGetRepos.repositories).isNotEmpty()
                assertThat(
                    itemGetRepos.repositories
                ).isEqualTo(fakeGithubRepositoryRepository.user1Repos)
                assertThat(fakeGithubRepositoryRepository.wasGetUserRepositoriesCalled).isTrue()
                assertThat(itemGetRepos.isLoading).isFalse()
            }
        }

    @Test
    fun `init getRepositoriesAndObserve when user haven't any repository unsuccessfully call getUserRepositories and state is not updated`() =
        runTest {
            setUp(userHaveAnyRepository = false, getUserRepositoriesReturnSuccess = false)
            userDetailsViewModel.state.test {
                val item1 = awaitItem()
                val itemLoading = awaitItem()
                val itemGetRepos = awaitItem()

                assertThat(item1.repositories).isEmpty()
                assertThat(itemLoading.isLoading).isTrue()
                assertThat(itemGetRepos.repositories).isEmpty()
                assertThat(itemGetRepos.apiError).isNotNull()
                assertThat(fakeGithubRepositoryRepository.wasGetUserRepositoriesCalled).isTrue()
                assertThat(itemGetRepos.isLoading).isFalse()
            }
        }
}

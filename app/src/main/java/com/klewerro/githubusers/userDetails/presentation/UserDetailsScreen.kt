@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.klewerro.githubusers.userDetails.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klewerro.githubusers.core.presentation.composable.SharedTransitionLayoutPreviewWrapper
import com.klewerro.githubusers.core.util.testData.GithubRepositoryTestData
import com.klewerro.githubusers.core.util.testData.UserDetailsTestData
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.userDetails.presentation.composable.GithubRepositoryListItem
import com.klewerro.githubusers.userDetails.presentation.composable.UserInformation
import com.klewerro.githubusers.users.presentation.GithubApiExceptionMessageConverter.convertGithubApiExceptionToUserMessage
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDetailsScreen(
    snackbarHostState: SnackbarHostState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userDetailsViewModel = koinViewModel<UserDetailsViewModel>()
    val state by userDetailsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.apiError) {
        state.apiError?.let {
            snackbarHostState.showSnackbar(
                message = it.convertGithubApiExceptionToUserMessage(context),
                duration = SnackbarDuration.Long
            )
            userDetailsViewModel.onEvent(UserDetailsEvent.DismissError)
        }
    }

    UserDetailsScreenContent(
        state = state,
        onEvent = { event ->
            userDetailsViewModel.onEvent(event)
        },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreenContent(
    state: UserDetailsState,
    onEvent: (UserDetailsEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = state.isLoading,
        onRefresh = {
            onEvent(UserDetailsEvent.RefreshData)
        }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                UserInformation(
                    user = state.user,
                    userDetails = state.userDetails,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(state.repositories) { repo ->
                GithubRepositoryListItem(repository = repo, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

// region preview
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserDetailsScreenPreview() {
    val state = UserDetailsState(
        user = UserTestData.user1,
        userDetails = UserDetailsTestData.userDetails1,
        repositories = listOf(
            GithubRepositoryTestData.repository1,
            GithubRepositoryTestData.repository1.copy(name = "Repo 2"),
            GithubRepositoryTestData.repository1.copy(name = "Repo 3")
        )
    )
    GithubUsersTheme {
        SharedTransitionLayoutPreviewWrapper {
            UserDetailsScreenContent(
                state = state,
                onEvent = {},
                sharedTransitionScope = it.first,
                animatedVisibilityScope = it.second
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsScreenIsLoadingPreview() {
    val state = UserDetailsState(
        user = UserTestData.user1,
        repositories = listOf(
            GithubRepositoryTestData.repository1,
            GithubRepositoryTestData.repository1.copy(name = "Repo 2"),
            GithubRepositoryTestData.repository1.copy(name = "Repo 3")
        ),
        isLoading = true
    )
    GithubUsersTheme {
        SharedTransitionLayoutPreviewWrapper {
            UserDetailsScreenContent(
                state = state,
                onEvent = {},
                sharedTransitionScope = it.first,
                animatedVisibilityScope = it.second
            )
        }
    }
}
// endregion

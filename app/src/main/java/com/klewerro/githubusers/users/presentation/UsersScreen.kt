package com.klewerro.githubusers.users.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
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
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.users.domain.model.User
import com.klewerro.githubusers.users.presentation.GithubApiExceptionMessageConverter.convertGithubApiExceptionToUserMessage
import com.klewerro.githubusers.users.presentation.composable.SearchTextField
import com.klewerro.githubusers.users.presentation.composable.UsersLazyColumn
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsersScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val usersViewModel = koinViewModel<UsersViewModel>()

    val state by usersViewModel.state.collectAsStateWithLifecycle()
    val userPager = usersViewModel.searchPager.collectAsLazyPagingItems()

    LaunchedEffect(userPager.loadState) {
        if (userPager.loadState.refresh is LoadState.Error) {
            val error =
                (userPager.loadState.refresh as LoadState.Error).error as GithubApiException
            val errorMessage = error.convertGithubApiExceptionToUserMessage(context)
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    UsersScreenContent(
        searchText = state.searchText,
        userPager = userPager,
        onEvent = usersViewModel::onEvent,
        isRefreshable = state.isRepositoryQueryNotBlank,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersScreenContent(
    searchText: String,
    userPager: LazyPagingItems<User>,
    onEvent: (UsersEvent) -> Unit,
    isRefreshable: Boolean,
    modifier: Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = userPager.loadState.refresh is LoadState.Loading,
        onRefresh = userPager::refresh,
        indicator = {
            if (isRefreshable) {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = userPager.loadState.refresh is LoadState.Loading,
                    state = pullToRefreshState
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchTextField(
                searchQuery = searchText,
                isEnabled = userPager.loadState.refresh != LoadState.Loading || !isRefreshable,
                onValueChange = {
                    onEvent(UsersEvent.SearchTextChanged(it))
                },
                onSearchClick = {
                    onEvent(UsersEvent.Search)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            )
            Spacer(modifier = Modifier.height(16.dp))
            UsersLazyColumn(
                userPager = userPager,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// region previews
@Preview(showBackground = true)
@Composable
private fun UsersScreenContentPreview() {
    val pagingFlow = flowOf(
        PagingData.from(
            listOf(
                UserTestData.user1,
                UserTestData.user2,
                UserTestData.user3
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    GithubUsersTheme {
        UsersScreenContent(
            "query",
            userPager = pagingFlow,
            onEvent = {},
            isRefreshable = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersScreenContentPreviewRefreshLoading() {
    val pagingFlow = flowOf(
        PagingData.from(
            listOf(
                UserTestData.user1,
                UserTestData.user2,
                UserTestData.user3
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Loading,
                append = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    GithubUsersTheme {
        UsersScreenContent(
            "query",
            userPager = pagingFlow,
            onEvent = {},
            isRefreshable = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersScreenContentPreviewAppendLoading() {
    val pagingFlow = flowOf(
        PagingData.from(
            listOf(
                UserTestData.user1,
                UserTestData.user2,
                UserTestData.user3
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                append = LoadState.Loading,
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    GithubUsersTheme {
        UsersScreenContent(
            "query",
            userPager = pagingFlow,
            onEvent = {},
            isRefreshable = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersScreenContentPreviewAppendLoadingError() {
    val pagingFlow = flowOf(
        PagingData.from(
            listOf(
                UserTestData.user1,
                UserTestData.user2,
                UserTestData.user3
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                append = LoadState.Error(
                    GithubApiException(GithubApiErrorType.RATE_LIMIT_EXCEEDED)
                ),
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    GithubUsersTheme {
        UsersScreenContent(
            "query",
            userPager = pagingFlow,
            onEvent = {},
            isRefreshable = false
        )
    }
}
// endregion

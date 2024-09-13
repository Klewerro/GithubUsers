package com.klewerro.githubusers.users.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.users.domain.model.User
import com.klewerro.githubusers.users.presentation.GithubApiExceptionMessageConverter.convertGithubApiExceptionToUserMessage
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsersScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val usersViewModel = koinViewModel<UsersViewModel>()
    val searchQuery by usersViewModel.searchText.collectAsStateWithLifecycle()
    val userPager = usersViewModel.searchPager.collectAsLazyPagingItems()

    LaunchedEffect(userPager.loadState) {
        if (userPager.loadState.refresh is LoadState.Error) {
            val error =
                (userPager.loadState.append as LoadState.Error).error as GithubApiException
            val errorMessage = error.convertGithubApiExceptionToUserMessage(context)
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    UsersScreenContent(
        searchQuery = searchQuery,
        userPager = userPager,
        onEvent = usersViewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun UsersScreenContent(
    searchQuery: String,
    userPager: LazyPagingItems<User>,
    onEvent: (UsersEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            searchQuery,
            onValueChange = {
                onEvent(UsersEvent.SearchTextChanged(it))
            },
            singleLine = true,
            trailingIcon = {
                FilledIconButton(
                    onClick = {
                        onEvent(UsersEvent.Search)
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(
                            R.string.search_for_users_using_provided_username
                        ),
                        tint = MaterialTheme.colorScheme.onPrimary

                    )
                }
            },
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (userPager.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    userPager.itemCount
                ) { index ->
                    val userItem = userPager[index]
                    if (userItem != null) {
                        UserItem(
                            user = userItem,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Todo: Propagate navigation event
                                }
                        )
                    }
                }
                item {
                    when (userPager.loadState.append) {
                        is LoadState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is LoadState.Error -> {
                            val error =
                                (userPager.loadState.append as LoadState.Error).error as GithubApiException
                            Text(
                                error.convertGithubApiExceptionToUserMessage(),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Button({ userPager.retry() }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        stringResource(R.string.retry_fetch_new_users)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.retry))
                                }
                            }
                        }

                        is LoadState.NotLoading -> Unit
                    }
                }
            }
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
            onEvent = {}
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
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersScreenContentPreviewAppendErrorLoading() {
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
            onEvent = {}
        )
    }
}
// endregion

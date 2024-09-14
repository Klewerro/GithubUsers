package com.klewerro.githubusers.users.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.users.domain.model.User
import com.klewerro.githubusers.users.presentation.GithubApiExceptionMessageConverter.convertGithubApiExceptionToUserMessage
import kotlinx.coroutines.flow.flowOf

@Composable
fun UsersLazyColumn(
    userPager: LazyPagingItems<User>,
    modifier: Modifier = Modifier,
    onUserClick: (User) -> Unit
) {
    LazyColumn(
        modifier = modifier,
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
                            onUserClick(userItem)
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

@PreviewLightDark
@Composable
private fun UsersLazyColumnPreview() {
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
        UsersLazyColumn(
            userPager = pagingFlow,
            onUserClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

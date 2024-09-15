@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.klewerro.githubusers.users.presentation.composable

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.presentation.composable.SharedTransitionLayoutPreviewWrapper
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.users.domain.model.User

@Composable
fun UserItem(
    user: User,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            with(sharedTransitionScope) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = "${user.login} ${stringResource(R.string.avatar)}",
                    modifier = Modifier
                        .weight(1f)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image/${user.login}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                with(sharedTransitionScope) {
                    Text(
                        text = user.login,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                state = rememberSharedContentState(key = "login/${user.login}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }

                Text(
                    text = '#' + user.id.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun UserItemPreview() {
    GithubUsersTheme {
        SharedTransitionLayoutPreviewWrapper {
            UserItem(
                user = UserTestData.user1,
                sharedTransitionScope = it.first,
                animatedVisibilityScope = it.second,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

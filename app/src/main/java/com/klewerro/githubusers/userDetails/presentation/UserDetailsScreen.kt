package com.klewerro.githubusers.userDetails.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.klewerro.githubusers.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserDetailsScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val userDetailsViewModel = koinViewModel<UserDetailsViewModel>()
    val user by userDetailsViewModel.user.collectAsStateWithLifecycle()
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = user.avatarUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "${user.login} ${stringResource(R.string.avatar)}",
                modifier = Modifier
                    .size(250.dp)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = "image/${user.login}/${user.avatarUrl}"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clip(RoundedCornerShape(8.dp))

            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = user.login,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "login/${user.login}"),
                        animatedVisibilityScope = animatedVisibilityScope

                    )
            )
        }
    }
}

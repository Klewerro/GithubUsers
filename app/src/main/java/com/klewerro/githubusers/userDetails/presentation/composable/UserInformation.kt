package com.klewerro.githubusers.userDetails.presentation.composable

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.klewerro.githubusers.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserInformation(
    login: String,
    avatarUrl: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = avatarUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "$login ${stringResource(R.string.avatar)}",
                modifier = Modifier
                    .size(250.dp)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = "image/$login"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clip(RoundedCornerShape(8.dp))

            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = login,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "login/$login"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
}

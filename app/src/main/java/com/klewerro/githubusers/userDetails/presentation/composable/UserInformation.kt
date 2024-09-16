package com.klewerro.githubusers.userDetails.presentation.composable

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.presentation.composable.TextIcon
import com.klewerro.githubusers.users.domain.model.User
import com.klewerro.githubusers.userDetails.domain.model.UserDetails

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserInformation(
    user: User,
    userDetails: UserDetails?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = user.avatarUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "${user.login} ${stringResource(R.string.avatar)}",
                modifier = Modifier
                    .size(250.dp)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = "image/${user.login}"
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
            if (userDetails != null) {
                TextIcon(
                    Icons.Default.Description,
                    userDetails.bio,
                    "User bio",
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

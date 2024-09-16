package com.klewerro.githubusers.userDetails.presentation.composable

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.presentation.composable.TextIcon
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.userDetails.domain.model.UserDetails
import com.klewerro.githubusers.users.domain.model.User

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
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
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(key = "login/${user.login}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
                userDetails?.name?.let {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "($it)",
                        style = MaterialTheme.typography.headlineMedium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        userDetails?.bio?.let {
            UserDetailsIconItem(
                Icons.Default.Description,
                it,
                "User bio"
            )
        }
        FlowRow(maxItemsInEachRow = 2) {
            userDetails?.email?.let {
                UserDetailsIconItem(
                    Icons.Default.Mail,
                    it,
                    "User email"
                )
            }
            userDetails?.location?.let {
                UserDetailsIconItem(
                    Icons.Default.LocationCity,
                    it,
                    "User location"
                )
            }
            userDetails?.company?.let {
                UserDetailsIconItem(
                    Icons.Default.Factory,
                    it,
                    "User company (work place)"
                )
            }
            userDetails?.hireable?.let {
                UserDetailsIconItem(
                    Icons.Default.Work,
                    "Hireable: ${if (it) "Yes" else "No"}",
                    "Is user hireable"
                )
            }
            userDetails?.blog?.let {
                if (it.isNotBlank()) {
                    UserDetailsIconItem(
                        Icons.Default.TextFields,
                        it,
                        "User blog"
                    )
                }
            }
            userDetails?.followersCount?.let {
                UserDetailsIconItem(
                    Icons.Default.PeopleAlt,
                    it.toString(),
                    "User followers count"
                )
            }
            userDetails?.followingCount?.let {
                UserDetailsIconItem(
                    Icons.Default.RemoveRedEye,
                    it.toString(),
                    "User's following by user count"
                )
            }

            userDetails?.createdAt?.let {
                UserDetailsTextItem(
                    "Created at:",
                    it.substringBefore(' '),
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            }
            userDetails?.updatedAt?.let {
                UserDetailsTextItem(
                    "Updated at:",
                    it.substringBefore(' '),
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            userDetails?.publicReposCount?.let {
                TextIcon(
                    Icons.Default.Code,
                    "Repos: $it",
                    "User's following by user count",
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
            userDetails?.publicReposCount?.let {
                TextIcon(
                    Icons.AutoMirrored.Filled.Note,
                    "Gists: $it",
                    "User's following by user count",
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    } // end with(sharedTransitionScope)
}

@Composable
fun UserDetailsIconItem(
    imageVector: ImageVector,
    text: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(
                with(LocalDensity.current) {
                    textStyle.fontSize.toDp()
                }
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = textStyle, color = contentColor)
    }
}

@Composable
fun UserDetailsTextItem(
    prefixText: String,
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = prefixText,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = textStyle, color = contentColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsIconItemPreview() {
    GithubUsersTheme {
        UserDetailsIconItem(
            Icons.Default.ChatBubble,
            text = "Some message",
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsTextItemPreview() {
    GithubUsersTheme {
        UserDetailsIconItem(
            Icons.Default.ChatBubble,
            text = "Some message",
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(.5f)
        )
    }
}

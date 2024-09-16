package com.klewerro.githubusers.userDetails.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.presentation.composable.TextIcon
import com.klewerro.githubusers.core.util.testData.GithubRepositoryTestData
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.userDetails.domain.model.GithubRepository

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GithubRepositoryListItem(repository: GithubRepository, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                Text(
                    text = repository.name,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis

                )
                Spacer(modifier = Modifier.width(16.dp))
                repository.language?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.tertiary)
                }
            }
            if (repository.description != null) {
                Text(
                    text = repository.description,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth()
            ) {
                DateBox("Last pushed at:", repository.pushedAt)
                DateBox("Created at:", repository.createdAt)
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextIcon(
                    R.drawable.issues,
                    repository.openIssuesCount.toString(),
                    "Open issues count"
                )
                TextIcon(
                    R.drawable.fork,
                    repository.forksCount.toString(),
                    "Forks count"
                )
                TextIcon(
                    Icons.Outlined.RemoveRedEye,
                    repository.watchersCount.toString(),
                    "Watchers count"
                )
                TextIcon(
                    Icons.Default.Star,
                    repository.stargazersCount.toString(),
                    "Stargazers count"
                )
            }
        }
    }
}

@Composable
private fun DateBox(headerText: String, dateText: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Text(
            text = headerText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = dateText,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// region preview
@PreviewLightDark
@Composable
private fun GithubRepositoryListItemPreview() {
    GithubUsersTheme {
        GithubRepositoryListItem(
            repository = GithubRepositoryTestData.repository1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun GithubRepositoryListItemBigNumbersPreview() {
    val bigNumber = 100_999
    val repository = GithubRepositoryTestData.repository1.copy(
        stargazersCount = bigNumber,
        forksCount = bigNumber,
        openIssuesCount = bigNumber,
        watchersCount = bigNumber
    )
    GithubUsersTheme {
        GithubRepositoryListItem(
            repository = repository,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
// endregion

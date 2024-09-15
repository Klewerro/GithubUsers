package com.klewerro.githubusers.core.util.testData

import com.klewerro.githubusers.userDetails.domain.GithubRepository

object GithubRepositoryTestData {
    val repository1 = GithubRepository(
        id = 1,
        name = "Repository1",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Fusce vitae odio placerat, malesuada tellus sit amet, lacinia ipsum. " +
            "Morbi vel libero in sapien hendrerit mattis. Praesent vel ante vitae enim" +
            " placerat volutpat non eu massa.",
        url = "https://repo.com",
        htmlUrl = "https://repo.com",
        createdAt = "25-10-23 14:49",
        pushedAt = "06-09-24 10:10",
        language = "Kotlin",
        stargazersCount = 3,
        watchersCount = 15,
        forksCount = 4,
        openIssuesCount = 2
    )
}

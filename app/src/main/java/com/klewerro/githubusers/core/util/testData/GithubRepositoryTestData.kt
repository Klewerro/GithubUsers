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
    val repository2 = GithubRepository(
        id = 2,
        name = "Repository2",
        description = "Example not popular repo description.",
        url = "https://repo2.com",
        htmlUrl = "https://repo2.com",
        createdAt = "22-12-23 15:39",
        pushedAt = "01-11-24 15:11",
        language = "Kotlin",
        stargazersCount = 0,
        watchersCount = 0,
        forksCount = 0,
        openIssuesCount = 0
    )
    val repository3 = GithubRepository(
        id = 3,
        name = "Example very popular repository.",
        description = ".",
        url = "https://repo3.com",
        htmlUrl = "https://repo3.com",
        createdAt = "12-01-20 10:09",
        pushedAt = "03-12-24 11:11",
        language = "Kotlin",
        stargazersCount = 10_002,
        watchersCount = 8_009,
        forksCount = 1218,
        openIssuesCount = 327
    )
}

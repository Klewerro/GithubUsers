package com.klewerro.githubusers.core.util.testData

import com.klewerro.githubusers.users.domain.model.User

object UserTestData {
    val user1 = User(
        id = 1,
        type = "User",
        login = "user1",
        url = "https://user1.com",
        avatarUrl = "https://user1_avatar.com",
        reposUrl = "https://user1_repos.com"
    )

    val user2 = User(
        id = 2,
        type = "User",
        login = "user2",
        url = "https://user2.com",
        avatarUrl = "https://user1_avatar.com",
        reposUrl = "https://user1_repos.com"
    )

    val user3 = User(
        id = 3,
        type = "User",
        login = "user3",
        url = "https://user3.com",
        avatarUrl = "https://user3_avatar.com",
        reposUrl = "https://user3_repos.com"
    )
}

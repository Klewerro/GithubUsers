package com.klewerro.githubusers.core.util.testData

import com.klewerro.githubusers.users.domain.model.UserDetails

object UserDetailsTestData {
    val userDetails1 = UserDetails(
        id = 1,
        login = "User1",
        name = "User1_name",
        location = "Warsaw",
        bio = "User1 bio informatinos",
        blog = "blog.address.com",
        company = "Company",
        email = "email@user1.com",
        hireable = true,
        publicGistsCount = 2,
        publicReposCount = 32,
        followersCount = 3,
        followingCount = 12,
        createdAt = "2016-05-10T19:12:36",
        updatedAt = "2024-09-15T13:48:39",
        url = "userUrl",
        inAppRefreshedAt = 12
    )
}

package com.klewerro.githubusers.userDetails.domain.model

data class UserDetails(
    val id: Int,
    val login: String,
    val name: String?,
    val location: String?,
    val bio: String?,
    val blog: String,
    val company: String?,
    val email: String?,
    val hireable: Boolean?,
    val publicGistsCount: Int,
    val publicReposCount: Int,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val url: String,
    val inAppRefreshedAt: Long
)

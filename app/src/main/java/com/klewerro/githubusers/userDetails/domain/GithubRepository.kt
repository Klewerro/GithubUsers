package com.klewerro.githubusers.userDetails.domain

data class GithubRepository(
    val id: Int,
    val name: String,
    val description: String?,
    val url: String,
    val htmlUrl: String,
    val createdAt: String,
    val language: String?,
    val pushedAt: String,
    val stargazersCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val openIssuesCount: Int
)

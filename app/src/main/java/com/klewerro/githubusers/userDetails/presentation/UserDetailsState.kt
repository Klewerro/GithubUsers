package com.klewerro.githubusers.userDetails.presentation

import com.klewerro.githubusers.userDetails.domain.GithubRepository
import com.klewerro.githubusers.users.domain.model.User

data class UserDetailsState(
    val user: User,
    val repositories: List<GithubRepository> = emptyList(),
    val isLoading: Boolean = false
)

package com.klewerro.githubusers.userDetails.presentation

import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.userDetails.domain.model.GithubRepository
import com.klewerro.githubusers.users.domain.model.User
import com.klewerro.githubusers.userDetails.domain.model.UserDetails

data class UserDetailsState(
    val user: User,
    val repositories: List<GithubRepository> = emptyList(),
    val userDetails: UserDetails? = null,
    val isLoading: Boolean = false,
    val apiError: GithubApiException? = null
)

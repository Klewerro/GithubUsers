package com.klewerro.githubusers.userDetails.domain

import com.klewerro.githubusers.userDetails.data.remote.GithubRepositoryDto

interface GithubRepositoryRemoteDataSource {
    suspend fun getUserRepositories(login: String): List<GithubRepositoryDto>
}

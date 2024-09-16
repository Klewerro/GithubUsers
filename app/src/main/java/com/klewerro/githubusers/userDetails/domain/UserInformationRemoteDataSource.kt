package com.klewerro.githubusers.userDetails.domain

import com.klewerro.githubusers.userDetails.data.remote.GithubRepositoryDto
import com.klewerro.githubusers.userDetails.data.remote.UserDetailsDto

interface UserInformationRemoteDataSource {
    suspend fun getUserRepositories(login: String): List<GithubRepositoryDto>
    suspend fun getUserDetails(login: String): UserDetailsDto
}

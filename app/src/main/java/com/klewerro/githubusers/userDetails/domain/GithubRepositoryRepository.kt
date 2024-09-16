package com.klewerro.githubusers.userDetails.domain

import kotlinx.coroutines.flow.Flow

interface GithubRepositoryRepository {

    fun observeUserRepositories(userId: Int): Flow<List<GithubRepository>>
    suspend fun getUserRepositories(userId: Int, login: String)
    suspend fun userHaveAnyRepository(userId: Int): Boolean
}

package com.klewerro.githubusers.userDetails.domain

import com.klewerro.githubusers.userDetails.domain.model.GithubRepository
import com.klewerro.githubusers.userDetails.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserInformationRepository {

    fun observeUserRepositories(userId: Int): Flow<List<GithubRepository>>
    suspend fun getUserRepositories(userId: Int, login: String)
    suspend fun userHaveAnyRepository(userId: Int): Boolean
    suspend fun getUserDetails(login: String)
    fun observeUserDetails(userId: Int): Flow<UserDetails>
    suspend fun userHaveSavedUserDetails(userId: Int): Boolean
}

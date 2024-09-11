package com.klewerro.githubusers.users.domain

import com.klewerro.githubusers.users.data.remote.dto.SearchForUsersResponseDto

interface UserDataSource {
    suspend fun searchForUsers(page: Int, perPage: Int, query: String): SearchForUsersResponseDto
}

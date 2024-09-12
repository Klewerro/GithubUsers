package com.klewerro.githubusers.users.data.remote

import com.klewerro.githubusers.core.data.ApiConstants
import com.klewerro.githubusers.core.data.runRequestThrowing
import com.klewerro.githubusers.users.data.remote.dto.SearchForUsersResponseDto
import com.klewerro.githubusers.users.domain.UserRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorUserRemoteDataSource(private val httpClient: HttpClient) : UserRemoteDataSource {
    override suspend fun searchForUsers(
        page: Int,
        perPage: Int,
        query: String
    ): SearchForUsersResponseDto = runRequestThrowing {
        httpClient.get(ApiConstants.SEARCH_FOR_USERS_URL) {
            parameter(ApiConstants.PARAM_PER_PAGE, perPage)
            parameter(ApiConstants.PARAM_PAGE, page)
            parameter(ApiConstants.PARAM_QUERY, query)
        }
    }
}

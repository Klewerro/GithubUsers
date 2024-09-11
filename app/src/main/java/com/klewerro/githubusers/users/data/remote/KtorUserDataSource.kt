package com.klewerro.githubusers.users.data.remote

import com.klewerro.githubusers.core.data.ApiConstants
import com.klewerro.githubusers.core.data.runThrowing
import com.klewerro.githubusers.users.data.remote.dto.SearchForUsersResponseDto
import com.klewerro.githubusers.users.domain.UserDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorUserDataSource(private val httpClient: HttpClient) : UserDataSource {
    override suspend fun searchForUsers(
        page: Int,
        perPage: Int,
        query: String
    ): SearchForUsersResponseDto = httpClient.get(ApiConstants.SEARCH_FOR_USERS_URL) {
        parameter(ApiConstants.PARAM_PER_PAGE, perPage)
        parameter(ApiConstants.PARAM_PAGE, page)
        parameter(ApiConstants.PARAM_QUERY, query)
    }.runThrowing<SearchForUsersResponseDto>()
}

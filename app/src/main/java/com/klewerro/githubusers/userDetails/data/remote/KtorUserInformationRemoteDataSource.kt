package com.klewerro.githubusers.userDetails.data.remote

import com.klewerro.githubusers.core.data.ApiConstants
import com.klewerro.githubusers.core.data.runRequestThrowing
import com.klewerro.githubusers.userDetails.domain.UserInformationRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorUserInformationRemoteDataSource(private val httpClient: HttpClient) :
    UserInformationRemoteDataSource {

    override suspend fun getUserRepositories(login: String): List<GithubRepositoryDto> =
        runRequestThrowing {
            httpClient.get(ApiConstants.userReposUrl(login)) {
                parameter(ApiConstants.PARAM_SORT, ApiConstants.VALUE_SORT_UPDATED)
            }
        }

    override suspend fun getUserDetails(login: String): UserDetailsDto = runRequestThrowing {
        httpClient.get(ApiConstants.userDetails(login))
    }
}

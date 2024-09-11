package com.klewerro.githubusers.users.data

import com.klewerro.githubusers.users.domain.UserDataSource
import com.klewerro.githubusers.users.domain.UserRepository
import timber.log.Timber

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    override suspend fun getUsers() {
        val searchForUsersResponse = userDataSource.searchForUsers(
            page = 1,
            perPage = 30,
            query = "Klewer"
        )
        val users = searchForUsersResponse.users
        Timber.d("Users in response count: ${users.size}")
        Timber.d(users.joinToString())
    }
}

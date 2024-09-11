package com.klewerro.githubusers.users.domain

interface UserRepository {
    suspend fun getUsers()
}

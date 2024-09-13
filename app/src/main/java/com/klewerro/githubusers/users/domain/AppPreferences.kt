package com.klewerro.githubusers.users.domain

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    val query: Flow<String>

    suspend fun setQuery(query: String)
}

package com.klewerro.githubusers.users.domain

import androidx.paging.PagingData
import com.klewerro.githubusers.users.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val searchQueryFlow: StateFlow<String>
    val searchPager: Flow<PagingData<User>>

    fun updateSearchQuery(searchQuery: String)
}

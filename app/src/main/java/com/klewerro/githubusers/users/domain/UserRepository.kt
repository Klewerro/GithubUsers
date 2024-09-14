package com.klewerro.githubusers.users.domain

import androidx.paging.PagingData
import com.klewerro.githubusers.users.data.local.UserEntity
import com.klewerro.githubusers.users.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val searchQueryFlow: Flow<String>
    val searchPager: Flow<PagingData<User>>

    suspend fun updateSearchQuery(searchQuery: String)
    fun createSearchPagerFlow(searchQuery: String): Flow<PagingData<UserEntity>>
}

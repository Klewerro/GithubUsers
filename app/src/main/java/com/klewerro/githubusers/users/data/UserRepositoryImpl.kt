package com.klewerro.githubusers.users.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.klewerro.githubusers.core.data.local.CacheDatabase
import com.klewerro.githubusers.core.util.AppConstants
import com.klewerro.githubusers.users.data.local.UserEntity
import com.klewerro.githubusers.users.data.mapper.toUser
import com.klewerro.githubusers.users.domain.UserRemoteDataSource
import com.klewerro.githubusers.users.domain.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val cacheDatabase: CacheDatabase
) : UserRepository {

    private val _searchQueryFlow = MutableStateFlow("Klewer") // Todo: Temporary
    override val searchQueryFlow = _searchQueryFlow.asStateFlow()

    override fun updateSearchQuery(searchQuery: String) {
        _searchQueryFlow.update { searchQuery }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val searchPager = searchQueryFlow
        .flatMapLatest { query ->
            createSearchForUsersRemoteMediator(query)
                .flow
                .map { pagingData ->
                    pagingData.map {
                        it.toUser()
                    }
                }
        }

    @OptIn(ExperimentalPagingApi::class)
    private fun createSearchForUsersRemoteMediator(searchQuery: String): Pager<Int, UserEntity> =
        Pager(
            config = PagingConfig(
                pageSize = AppConstants.PAGING_PAGE_SIZE,
                initialLoadSize = AppConstants.PAGING_INITIAL_LOAD_SIZE
            ),
            remoteMediator = SearchForUsersRemoteMediator(
                cacheDatabase = cacheDatabase,
                userRemoteDataSource = userRemoteDataSource,
                searchQuery = searchQuery
            ),
            pagingSourceFactory = {
                cacheDatabase.userDao.pagingSource()
            }
        )
}

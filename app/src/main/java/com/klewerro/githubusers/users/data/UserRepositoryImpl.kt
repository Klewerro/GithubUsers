package com.klewerro.githubusers.users.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.klewerro.githubusers.core.data.local.CacheDatabase
import com.klewerro.githubusers.core.util.AppConstants
import com.klewerro.githubusers.users.data.local.UserEntity
import com.klewerro.githubusers.users.data.mapper.toUser
import com.klewerro.githubusers.users.domain.AppPreferences
import com.klewerro.githubusers.users.domain.UserRemoteDataSource
import com.klewerro.githubusers.users.domain.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val cacheDatabase: CacheDatabase,
    private val appPreferences: AppPreferences
) : UserRepository {

    private var firstRefreshSkipped = false
    private var searchQueryUpdatedFromEmptyText = false

    override val searchQueryFlow = appPreferences.query

    override suspend fun updateSearchQuery(searchQuery: String) {
        searchQueryUpdatedFromEmptyText = searchQueryFlow.first().isBlank()
        Timber.d("searchQueryUpdatedFromEmptyText: $searchQueryUpdatedFromEmptyText")
        appPreferences.setQuery(searchQuery)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val searchPager = searchQueryFlow
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            createSearchForUsersRemoteMediator(query)
                .flow
                .map { pagingData ->
                    pagingData.map {
                        it.toUser()
                    }
                }
        }

    override fun createSearchPagerFlow(searchQuery: String) =
        createSearchForUsersRemoteMediator(searchQuery)
            .flow

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
                searchQuery = searchQuery,
                skippFirstRefresh = {
                    if (searchQueryUpdatedFromEmptyText) {
                        // Not skipping first refresh, because that mechanism already happened
                        // because of .filter { it.isNotBlank() } in searchPager flow field.
                        false
                    } else {
                        val shouldSkip = !firstRefreshSkipped
                        if (!firstRefreshSkipped) {
                            firstRefreshSkipped = true
                        }
                        shouldSkip
                    }
                }
            ),
            pagingSourceFactory = {
                cacheDatabase.userDao.pagingSource()
            }
        )
}

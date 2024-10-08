package com.klewerro.githubusers.users.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.InitializeAction
import androidx.room.withTransaction
import com.klewerro.githubusers.core.data.error.GithubApiException
import com.klewerro.githubusers.core.data.local.CacheDatabase
import com.klewerro.githubusers.users.data.local.UserEntity
import com.klewerro.githubusers.users.data.mapper.toUserEntity
import com.klewerro.githubusers.users.domain.UserRemoteDataSource
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

/**
 * Class using for synchronization between remote and local data sources.
 * Omitting first refresh for presenting already collected data to the user.
 *
 * @param skippFirstRefresh is used to pass value from repository to determine, if first refresh was
 * omitted or not.
 *
 * @see LoadType
 * @see InitializeAction
 */
@OptIn(ExperimentalPagingApi::class)
class SearchForUsersRemoteMediator(
    private val cacheDatabase: CacheDatabase,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val searchQuery: String,
    private val skippFirstRefresh: () -> Boolean
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val pageSize = state.config.pageSize
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    if (skippFirstRefresh()) {
                        Timber.d("First refresh skipped")
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }

                    if (searchQuery.isNotBlank()) {
                        1 // start from the 1st page
                    } else {
                        return MediatorResult.Error(Exception("Empty search query"))
                    }
                }
                // only appending, unsupported
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val savedUsersCount = cacheDatabase.userDao.count()
                    if (savedUsersCount == 0) {
                        1
                    } else {
                        val page = (savedUsersCount / pageSize) + 1
                        page
                    }
                }
            }

            delay(3.seconds)
            Timber.d("loadKey: $loadKey")
            Timber.d("LoadType: $loadType")
            val searchForUsersResponse = userRemoteDataSource.searchForUsers(
                page = loadKey,
                perPage = pageSize,
                query = searchQuery
            )
            cacheDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Clear database in case of successful fetch after refresh
                    cacheDatabase.userDao.deleteAll()
                    Timber.d("Database cleared")
                }

                val currentTimeMillis = System.currentTimeMillis()
                val maxInsertionNumber = cacheDatabase.userDao.count()
                val mappedUsers = searchForUsersResponse.users.mapIndexed { index, userDto ->
                    userDto.toUserEntity(
                        currentTimeMillis = currentTimeMillis,
                        insertionNumber = maxInsertionNumber + index + 1
                    )
                }
                cacheDatabase.userDao.upsertAll(mappedUsers)
            }
            MediatorResult.Success(
                endOfPaginationReached = searchForUsersResponse.users.size < pageSize
            )
        } catch (e: GithubApiException) {
            Timber.e(e, "Exception in Mediator")
            MediatorResult.Error(e)
        }
    }
}

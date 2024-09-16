package com.klewerro.githubusers.userDetails.data

import com.klewerro.githubusers.userDetails.data.local.githubRepository.GithubRepositoryDao
import com.klewerro.githubusers.userDetails.data.local.userDetails.UserDetailsDao
import com.klewerro.githubusers.userDetails.data.mapper.toEntity
import com.klewerro.githubusers.userDetails.data.mapper.toGithubRepository
import com.klewerro.githubusers.userDetails.data.mapper.toUserDetails
import com.klewerro.githubusers.userDetails.data.mapper.toUserDetailsEntity
import com.klewerro.githubusers.userDetails.domain.GithubRepository
import com.klewerro.githubusers.userDetails.domain.UserInformationRemoteDataSource
import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.users.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInformationRepositoryImpl(
    private val githubRepositoryDao: GithubRepositoryDao,
    private val userDetailsDao: UserDetailsDao,
    private val userInformationRemoteDataSource: UserInformationRemoteDataSource
) : UserInformationRepository {

    override fun observeUserRepositories(userId: Int): Flow<List<GithubRepository>> =
        githubRepositoryDao
            .getUserRepositories(userId)
            .map {
                it.map { githubRepositoryEntity ->
                    githubRepositoryEntity.toGithubRepository()
                }
            }

    override suspend fun userHaveAnyRepository(userId: Int) =
        githubRepositoryDao.userRepositoriesCount(userId) > 0

    override suspend fun getUserRepositories(userId: Int, login: String) {
        val repos = userInformationRemoteDataSource.getUserRepositories(login)
        val mappedRepos = repos.map {
            it.toEntity(userId)
        }
        githubRepositoryDao.upsertAll(mappedRepos)
    }

    // Observe userDetails
    override fun observeUserDetails(userId: Int): Flow<UserDetails> = userDetailsDao
        .getUserDetails(userId)
        .map {
            it.toUserDetails()
        }

    override suspend fun userHaveSavedUserDetails(userId: Int) = userDetailsDao.isExist(userId)

    override suspend fun getUserDetails(login: String) {
        val dto = userInformationRemoteDataSource.getUserDetails(login)
        val currentTime = System.currentTimeMillis()
        val entity = dto.toUserDetailsEntity(currentTime)
        userDetailsDao.upsertItem(entity)
    }
}

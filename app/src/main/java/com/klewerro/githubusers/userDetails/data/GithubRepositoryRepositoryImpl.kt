package com.klewerro.githubusers.userDetails.data

import com.klewerro.githubusers.userDetails.data.local.GithubRepositoryDao
import com.klewerro.githubusers.userDetails.data.mapper.toEntity
import com.klewerro.githubusers.userDetails.data.mapper.toGithubRepository
import com.klewerro.githubusers.userDetails.domain.GithubRepository
import com.klewerro.githubusers.userDetails.domain.GithubRepositoryRemoteDataSource
import com.klewerro.githubusers.userDetails.domain.GithubRepositoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GithubRepositoryRepositoryImpl(
    private val githubRepositoryDao: GithubRepositoryDao,
    private val githubRepositoryRemoteDataSource: GithubRepositoryRemoteDataSource
) : GithubRepositoryRepository {

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
        val repos = githubRepositoryRemoteDataSource.getUserRepositories(login)
        val mappedRepos = repos.map {
            it.toEntity(userId)
        }
        githubRepositoryDao.upsertAll(mappedRepos)
    }
}

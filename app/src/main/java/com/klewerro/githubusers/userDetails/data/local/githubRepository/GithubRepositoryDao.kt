package com.klewerro.githubusers.userDetails.data.local.githubRepository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubRepositoryDao {

    @Query("SELECT * from github_repository where user_id == :userId")
    fun getUserRepositories(userId: Int): Flow<List<GithubRepositoryEntity>>

    @Query("SELECT COUNT(id) from github_repository where user_id == :userId")
    suspend fun userRepositoriesCount(userId: Int): Int

    @Upsert
    suspend fun upsertAll(repositories: List<GithubRepositoryEntity>)

    @Query("DELETE from github_repository")
    suspend fun deleteAll()
}

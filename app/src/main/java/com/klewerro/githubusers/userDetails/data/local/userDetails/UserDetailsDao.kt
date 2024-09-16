package com.klewerro.githubusers.userDetails.data.local.userDetails

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailsDao {

    @Query("SELECT * from user_details where id == :id limit 1")
    fun getUserDetails(id: Int): Flow<UserDetailsEntity>

    @Upsert
    suspend fun upsertItem(userDetailsEntity: UserDetailsEntity)

    @Query("SELECT COUNT(id) from github_repository where user_id = :id limit 1")
    suspend fun isExist(id: Int): Boolean

    @Query("DELETE from user_details")
    suspend fun deleteAll()
}

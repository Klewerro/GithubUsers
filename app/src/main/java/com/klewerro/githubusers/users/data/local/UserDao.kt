package com.klewerro.githubusers.users.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * from user")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * from user order by insertionNumber")
    fun pagingSource(): PagingSource<Int, UserEntity>

    @Query("SELECT COUNT(id) from user")
    suspend fun count(): Int

    @Query("SELECT MAX(id) from user")
    suspend fun getMaxInsertedId(): Int

    @Upsert
    suspend fun upsertAll(users: List<UserEntity>)

    @Query("DELETE from user")
    suspend fun deleteAll()
}

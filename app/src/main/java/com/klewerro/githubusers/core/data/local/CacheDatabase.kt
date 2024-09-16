package com.klewerro.githubusers.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.klewerro.githubusers.userDetails.data.local.githubRepository.GithubRepositoryDao
import com.klewerro.githubusers.userDetails.data.local.githubRepository.GithubRepositoryEntity
import com.klewerro.githubusers.userDetails.data.local.userDetails.UserDetailsDao
import com.klewerro.githubusers.userDetails.data.local.userDetails.UserDetailsEntity
import com.klewerro.githubusers.users.data.local.UserDao
import com.klewerro.githubusers.users.data.local.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GithubRepositoryEntity::class,
        UserDetailsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val githubRepositoryDao: GithubRepositoryDao
    abstract val userDetailsDao: UserDetailsDao
}

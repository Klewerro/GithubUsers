package com.klewerro.githubusers.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.klewerro.githubusers.users.data.local.UserDao
import com.klewerro.githubusers.users.data.local.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}

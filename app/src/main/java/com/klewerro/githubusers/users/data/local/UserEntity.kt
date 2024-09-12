package com.klewerro.githubusers.users.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    val type: String,
    val login: String,
    val url: String,
    val avatarUrl: String,
    val reposUrl: String,
    val insertedTime: Long,
    val insertionNumber: Int
)

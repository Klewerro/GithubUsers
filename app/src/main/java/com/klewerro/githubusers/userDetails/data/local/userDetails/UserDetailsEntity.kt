package com.klewerro.githubusers.userDetails.data.local.userDetails

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetailsEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val name: String,
    val location: String,
    val bio: String,
    val blog: String,
    val company: String?,
    val email: String?,
    val hireable: Boolean?,
    @ColumnInfo("public_gists_count")
    val publicGistsCount: Int,
    @ColumnInfo("public_repos_count")
    val publicReposCount: Int,
    @ColumnInfo("followers_count")
    val followersCount: Int,
    @ColumnInfo("following_count")
    val followingCount: Int,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("in_app_refreshed_at")
    val inAppRefreshedAt: Long
)

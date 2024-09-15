package com.klewerro.githubusers.userDetails.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "github_repository",
    indices = [
        Index(value = ["user_id"])
    ]
)
data class GithubRepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val url: String,
    @ColumnInfo(name = "html_url")
    val htmlUrl: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    val language: String?,
    @ColumnInfo(name = "pushed_at")
    val pushedAt: String,
    @ColumnInfo(name = "stargazers_count")
    val stargazersCount: Int,
    @ColumnInfo(name = "watchers_count")
    val watchersCount: Int,
    @ColumnInfo(name = "forks_count")
    val forksCount: Int,
    @ColumnInfo(name = "open_issues_count")
    val openIssuesCount: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int
)

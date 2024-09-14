package com.klewerro.githubusers.userDetails.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String?,
    @SerialName("url")
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("archived")
    val archived: Boolean,
    @SerialName("clone_url")
    val cloneUrl: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("fork")
    val fork: Boolean,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("has_discussions")
    val hasDiscussions: Boolean,
    @SerialName("has_issues")
    val hasIssues: Boolean,
    @SerialName("has_pages")
    val hasPages: Boolean,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("is_template")
    val isTemplate: Boolean,
    @SerialName("language")
    val language: String?,
    @SerialName("languages_url")
    val languagesUrl: String,
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    @SerialName("private")
    val isPrivate: Boolean,
    @SerialName("pushed_at")
    val pushedAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    /**
     * Size in kolobytes
     */
    @SerialName("size")
    val size: Int,
    @SerialName("ssh_url")
    val sshUrl: String,
    @SerialName("visibility")
    val visibility: String,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int
)

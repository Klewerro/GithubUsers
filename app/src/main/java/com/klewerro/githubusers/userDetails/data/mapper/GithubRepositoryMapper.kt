package com.klewerro.githubusers.userDetails.data.mapper

import com.klewerro.githubusers.core.util.convertIso8601DateToLocalDateTime
import com.klewerro.githubusers.userDetails.data.local.githubRepository.GithubRepositoryEntity
import com.klewerro.githubusers.userDetails.data.remote.GithubRepositoryDto
import com.klewerro.githubusers.userDetails.domain.model.GithubRepository

fun GithubRepositoryEntity.toGithubRepository() = GithubRepository(
    id = this.id,
    name = this.name,
    description = this.description,
    url = this.url,
    htmlUrl = this.htmlUrl,
    createdAt = convertIso8601DateToLocalDateTime(this.createdAt),
    pushedAt = convertIso8601DateToLocalDateTime(this.pushedAt),
    language = this.language,
    stargazersCount = this.stargazersCount,
    watchersCount = this.watchersCount,
    forksCount = this.forksCount,
    openIssuesCount = this.openIssuesCount
)

fun GithubRepositoryDto.toEntity(userId: Int): GithubRepositoryEntity = GithubRepositoryEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    url = this.url,
    htmlUrl = this.htmlUrl,
    createdAt = this.createdAt,
    pushedAt = this.pushedAt,
    language = this.language,
    stargazersCount = this.stargazersCount,
    watchersCount = this.watchersCount,
    forksCount = this.forksCount,
    openIssuesCount = this.openIssuesCount,
    userId = userId
)

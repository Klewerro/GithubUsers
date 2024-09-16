package com.klewerro.githubusers.userDetails.data.mapper

import com.klewerro.githubusers.userDetails.data.local.githubRepository.GithubRepositoryEntity
import com.klewerro.githubusers.userDetails.data.remote.GithubRepositoryDto
import com.klewerro.githubusers.userDetails.domain.model.GithubRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun GithubRepositoryEntity.toGithubRepository() = GithubRepository(
    id = this.id,
    name = this.name,
    description = this.description,
    url = this.url,
    htmlUrl = this.htmlUrl,
    createdAt = this.createdAt,
    language = this.language,
    pushedAt = this.pushedAt,
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
    createdAt = convertIso8601DateToLocalDateTime(this.createdAt),
    pushedAt = convertIso8601DateToLocalDateTime(this.pushedAt),
    language = this.language,
    stargazersCount = this.stargazersCount,
    watchersCount = this.watchersCount,
    forksCount = this.forksCount,
    openIssuesCount = this.openIssuesCount,
    userId = userId
)

/**
 * Drops last sign (z, meaning UTC time) from ISO 8601 date time text and convert it to
 * Java LocalDateTime.
 */
private fun convertIso8601DateToLocalDateTime(iso8601DateTimeText: String): String {
    val date = LocalDateTime.parse(iso8601DateTimeText.dropLast(1))
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")
    val formattedDate = date.format(formatter)
    return formattedDate
}

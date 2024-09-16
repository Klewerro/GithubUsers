package com.klewerro.githubusers.userDetails.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsDto(
    val id: Int,
    val login: String,
    val name: String,
    val location: String,
    val bio: String,
    val blog: String,
    val company: String?,
    @SerialName("created_at")
    val createdAt: String,
    val email: String?,
    @SerialName("events_url")
    val eventsUrl: String,
    @SerialName("followers_url")
    val followersUrl: String,
    @SerialName("following")
    val followingCount: Int,
    @SerialName("following_url")
    val followingUrl: String,
    @SerialName("gists_url")
    val gistsUrl: String,
    val hireable: Boolean?,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("organizations_url")
    val organizationsUrl: String,
    @SerialName("public_gists")
    val publicGistsCount: Int,
    @SerialName("public_repos")
    val publicReposCount: Int,
    @SerialName("followers")
    val followersCount: Int,
    @SerialName("site_admin")
    val siteAdmin: Boolean,
    @SerialName("twitter_username")
    val twitterUsername: String?,
    @SerialName("type")
    val type: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("url")
    val url: String
)

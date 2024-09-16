package com.klewerro.githubusers.userDetails.data.mapper

import com.klewerro.githubusers.userDetails.data.local.userDetails.UserDetailsEntity
import com.klewerro.githubusers.userDetails.data.remote.UserDetailsDto
import com.klewerro.githubusers.users.domain.model.UserDetails

fun UserDetailsEntity.toUserDetails() = UserDetails(
    id = this.id,
    login = this.login,
    name = this.name,
    location = this.location,
    bio = this.bio,
    blog = this.blog,
    company = this.company,
    email = this.email,
    hireable = this.hireable,
    publicGistsCount = this.publicGistsCount,
    publicReposCount = this.publicReposCount,
    followersCount = this.followersCount,
    followingCount = this.followingCount,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    url = this.url,
    inAppRefreshedAt = this.inAppRefreshedAt
)

fun UserDetailsDto.toUserDetailsEntity(refreshTimeInMillis: Long) = UserDetailsEntity(
    id = this.id,
    login = this.login,
    name = this.name,
    location = this.location,
    bio = this.bio,
    blog = this.blog,
    company = this.company,
    email = this.email,
    hireable = this.hireable,
    publicGistsCount = this.publicGistsCount,
    publicReposCount = this.publicReposCount,
    followersCount = this.followersCount,
    followingCount = this.followingCount,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    url = this.url,
    inAppRefreshedAt = refreshTimeInMillis
)

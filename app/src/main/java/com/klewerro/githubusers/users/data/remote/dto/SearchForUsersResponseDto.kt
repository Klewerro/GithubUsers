package com.klewerro.githubusers.users.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchForUsersResponseDto(
    @SerialName("incomplete_results")
    val incompleteResults: Boolean,
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("items")
    val users: List<UserDto>
)

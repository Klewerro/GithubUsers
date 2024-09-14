package com.klewerro.githubusers.core.presentation.navigation

import com.klewerro.githubusers.users.domain.model.User
import kotlinx.serialization.Serializable

object NavRoutes {
    @Serializable
    object SearchUsersScreen

    @Serializable
    data class UserDetailsScreen(val user: User)
}

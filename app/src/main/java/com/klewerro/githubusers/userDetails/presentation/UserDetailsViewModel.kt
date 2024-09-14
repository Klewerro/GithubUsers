package com.klewerro.githubusers.userDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.klewerro.githubusers.core.presentation.navigation.CustomNavType
import com.klewerro.githubusers.core.presentation.navigation.NavRoutes
import com.klewerro.githubusers.users.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.typeOf

class UserDetailsViewModel(savedState: SavedStateHandle) : ViewModel() {

    private val _user = MutableStateFlow(
        savedState.toRoute<NavRoutes.UserDetailsScreen>(
            typeMap = mapOf(typeOf<User>() to CustomNavType.UserType)
        ).user
    )
    val user = _user.asStateFlow()
}

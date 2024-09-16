package com.klewerro.githubusers.core.presentation.savedState

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.klewerro.githubusers.core.presentation.navigation.CustomNavType
import com.klewerro.githubusers.core.presentation.navigation.NavRoutes
import com.klewerro.githubusers.users.domain.model.User
import kotlin.reflect.typeOf

class AndroidSavedStateHandleProvider(private val savedStateHandle: SavedStateHandle) :
    SavedStateProvider {

    override fun getUser(): User = savedStateHandle.toRoute<NavRoutes.UserDetailsScreen>(
        typeMap = mapOf(typeOf<User>() to CustomNavType.UserType)
    ).user
}

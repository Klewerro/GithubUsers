package com.klewerro.githubusers.core.presentation.savedState

import com.klewerro.githubusers.users.domain.model.User

/**
 * Contract for Android framework dependent SavedStateHandle.
 * Created mainly for new type-safe navigation testing purposes.
 *
 * @see AndroidSavedStateHandleProvider
 */
interface SavedStateProvider {
    fun getUser(): User
}

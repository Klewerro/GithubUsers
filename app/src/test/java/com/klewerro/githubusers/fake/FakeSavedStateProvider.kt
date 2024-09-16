package com.klewerro.githubusers.fake

import com.klewerro.githubusers.core.presentation.savedState.SavedStateProvider
import com.klewerro.githubusers.core.util.testData.UserTestData
import com.klewerro.githubusers.users.domain.model.User

class FakeSavedStateProvider : SavedStateProvider {

    var savedUser = UserTestData.user1

    override fun getUser(): User = savedUser
}

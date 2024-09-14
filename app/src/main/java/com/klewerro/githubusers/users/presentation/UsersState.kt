package com.klewerro.githubusers.users.presentation

data class UsersState(val searchText: String = "", val isRepositoryQueryNotBlank: Boolean = false)

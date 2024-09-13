package com.klewerro.githubusers.users.presentation

sealed class UsersEvent {
    data class SearchTextChanged(val text: String) : UsersEvent()
    data object Search : UsersEvent()
}

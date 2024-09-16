package com.klewerro.githubusers.userDetails.presentation

sealed class UserDetailsEvent {
    data object DismissError : UserDetailsEvent()
    data object RefreshData : UserDetailsEvent()
}

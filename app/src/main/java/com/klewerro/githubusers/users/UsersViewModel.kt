package com.klewerro.githubusers.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klewerro.githubusers.users.domain.UserRepository
import kotlinx.coroutines.launch

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUsers() {
        viewModelScope.launch {
            userRepository.getUsers()
        }
    }
}

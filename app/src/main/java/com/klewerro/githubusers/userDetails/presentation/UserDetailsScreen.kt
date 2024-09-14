package com.klewerro.githubusers.userDetails.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDetailsScreen(modifier: Modifier = Modifier) {
    val userDetailsViewModel = koinViewModel<UserDetailsViewModel>()
    val user by userDetailsViewModel.user.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = user.login, modifier = Modifier.align(Alignment.Center))
    }
}

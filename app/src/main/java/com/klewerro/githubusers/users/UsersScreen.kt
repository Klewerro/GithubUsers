package com.klewerro.githubusers.users

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsersScreen(modifier: Modifier = Modifier) {
    val usersViewModel = koinViewModel<UsersViewModel>()
    Row(modifier = modifier.fillMaxSize()) {
        Button(onClick = usersViewModel::getUsers) {
            Text("Search users")
        }
    }
}

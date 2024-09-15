package com.klewerro.githubusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klewerro.githubusers.core.presentation.navigation.CustomNavType
import com.klewerro.githubusers.core.presentation.navigation.NavRoutes
import com.klewerro.githubusers.ui.theme.GithubUsersTheme
import com.klewerro.githubusers.userDetails.presentation.UserDetailsScreen
import com.klewerro.githubusers.users.domain.model.User
import com.klewerro.githubusers.users.presentation.UsersScreen
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubUsersTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(R.string.app_name))
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    SharedTransitionLayout {
                        val navController = rememberNavController()
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .padding(16.dp)
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = NavRoutes.SearchUsersScreen,
                                enterTransition = { slideInHorizontally { it } + fadeIn() },
                                exitTransition = { slideOutHorizontally { -it } + fadeOut() },
                                popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
                                popExitTransition = { slideOutHorizontally { it } + fadeOut() }
                            ) {
                                composable<NavRoutes.SearchUsersScreen> {
                                    UsersScreen(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this,
                                        onUserClick = { user ->
                                            navController.navigate(
                                                NavRoutes.UserDetailsScreen(user)
                                            )
                                        }
                                    )
                                }
                                composable<NavRoutes.UserDetailsScreen>(
                                    typeMap = mapOf(typeOf<User>() to CustomNavType.UserType)
                                ) {
                                    UserDetailsScreen(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

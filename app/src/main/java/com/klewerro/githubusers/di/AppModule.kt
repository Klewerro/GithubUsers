package com.klewerro.githubusers.di

import androidx.room.Room
import com.klewerro.githubusers.core.data.local.CacheDatabase
import com.klewerro.githubusers.core.domain.dispatcher.DispatcherProvider
import com.klewerro.githubusers.core.domain.dispatcher.StandardDispatchers
import com.klewerro.githubusers.userDetails.data.UserInformationRepositoryImpl
import com.klewerro.githubusers.userDetails.data.local.githubRepository.GithubRepositoryDao
import com.klewerro.githubusers.userDetails.data.local.userDetails.UserDetailsDao
import com.klewerro.githubusers.userDetails.data.remote.KtorUserInformationRemoteDataSource
import com.klewerro.githubusers.userDetails.domain.UserInformationRemoteDataSource
import com.klewerro.githubusers.userDetails.domain.UserInformationRepository
import com.klewerro.githubusers.userDetails.presentation.UserDetailsViewModel
import com.klewerro.githubusers.users.data.UserRepositoryImpl
import com.klewerro.githubusers.users.data.local.keyValue.DataStoreAppPreferences
import com.klewerro.githubusers.users.data.remote.KtorUserRemoteDataSource
import com.klewerro.githubusers.users.domain.AppPreferences
import com.klewerro.githubusers.users.domain.UserRemoteDataSource
import com.klewerro.githubusers.users.domain.UserRepository
import com.klewerro.githubusers.users.presentation.UsersViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        UsersViewModel(userRepository = get())
    }
    viewModel {
        UserDetailsViewModel(
            savedState = get(),
            userInformationRepository = get(),
            dispatches = get(),
            getAndObserveUserDetailsUseCase = get(),
            getAndObserveRepositoriesUseCase = get()
        )
    }
    single<UserRepository> {
        UserRepositoryImpl(
            userRemoteDataSource = get(),
            cacheDatabase = get(),
            appPreferences = get()
        )
    }
    single<UserRemoteDataSource> {
        KtorUserRemoteDataSource(httpClient = get())
    }
    single<AppPreferences> {
        DataStoreAppPreferences(context = androidContext())
    }

    single<UserInformationRepository> {
        UserInformationRepositoryImpl(
            githubRepositoryDao = get(),
            userDetailsDao = get(),
            userInformationRemoteDataSource = get()
        )
    }
    single<GithubRepositoryDao> {
        get<CacheDatabase>().githubRepositoryDao
    }
    single<UserDetailsDao> {
        get<CacheDatabase>().userDetailsDao
    }
    single<UserInformationRemoteDataSource> {
        KtorUserInformationRemoteDataSource(httpClient = get())
    }

    single<DispatcherProvider> {
        StandardDispatchers()
    }
    single {
        Room.databaseBuilder(
            androidContext(),
            CacheDatabase::class.java,
            "github_users_cache_db"
        ).build()
    }
    single {
        HttpClient {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}

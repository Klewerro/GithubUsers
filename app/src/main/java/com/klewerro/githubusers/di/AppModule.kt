package com.klewerro.githubusers.di

import com.klewerro.githubusers.users.UsersViewModel
import com.klewerro.githubusers.users.data.UserRepositoryImpl
import com.klewerro.githubusers.users.data.remote.KtorUserDataSource
import com.klewerro.githubusers.users.domain.UserDataSource
import com.klewerro.githubusers.users.domain.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        UsersViewModel(userRepository = get())
    }
    single<UserRepository> {
        UserRepositoryImpl(userDataSource = get())
    }
    single<UserDataSource> {
        KtorUserDataSource(httpClient = get())
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

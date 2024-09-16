package com.klewerro.githubusers

import android.app.Application
import com.klewerro.githubusers.di.appModule
import com.klewerro.githubusers.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class GithubUsersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupLogging()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@GithubUsersApplication)
            modules(appModule, viewModelModule)
        }
    }
}

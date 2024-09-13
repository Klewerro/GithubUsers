package com.klewerro.githubusers.users.data.local.keyValue

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.klewerro.githubusers.users.domain.AppPreferences
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreAppPreferences(context: Context) : AppPreferences {

    private val dataStore = context.dataStore

    override val query = dataStore.data
        .map { preferences ->
            preferences[queryKey] ?: ""
        }

    override suspend fun setQuery(query: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[queryKey] = query
        }
    }

    private companion object {
        val queryKey = stringPreferencesKey("query")
    }
}

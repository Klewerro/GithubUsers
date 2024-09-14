package com.klewerro.githubusers.core.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.klewerro.githubusers.users.domain.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val UserType = object : NavType<User>(isNullableAllowed = false) {
        // Probably for compatibility reasons for fragment impl
        override fun get(bundle: Bundle, key: String): User? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): User = Json.decodeFromString(Uri.decode(value))

        override fun serializeAsValue(value: User): String = Uri.encode(Json.encodeToString(value))

        // Probably for compatibility reasons for fragment impl
        override fun put(bundle: Bundle, key: String, value: User) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}

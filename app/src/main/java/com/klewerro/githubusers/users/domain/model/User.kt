package com.klewerro.githubusers.users.domain.model

class User(
    val id: Int,
    val type: String,
    val login: String,
    val url: String,
    val avatarUrl: String,
    val reposUrl: String
)

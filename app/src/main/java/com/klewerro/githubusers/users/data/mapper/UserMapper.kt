package com.klewerro.githubusers.users.data.mapper

import com.klewerro.githubusers.users.data.local.UserEntity
import com.klewerro.githubusers.users.data.remote.dto.UserDto
import com.klewerro.githubusers.users.domain.model.User

fun UserEntity.toUser() = User(
    id = this.id,
    type = this.type,
    login = this.login,
    url = this.url,
    avatarUrl = this.avatarUrl,
    reposUrl = this.reposUrl
)

fun UserDto.toUserEntity(currentTimeMillis: Long, insertionNumber: Int) = UserEntity(
    id = this.id,
    type = this.type,
    login = this.login,
    url = this.url,
    avatarUrl = this.avatarUrl,
    reposUrl = this.reposUrl,
    insertedTime = currentTimeMillis,
    insertionNumber = insertionNumber
)
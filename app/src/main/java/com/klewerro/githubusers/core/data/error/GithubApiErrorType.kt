package com.klewerro.githubusers.core.data.error

enum class GithubApiErrorType {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    SERIALIZATION_ERROR,
    RATE_LIMIT_EXCEEDED,
    UNPROCESSABLE_QUERY,
    UNKNOWN_ERROR
}

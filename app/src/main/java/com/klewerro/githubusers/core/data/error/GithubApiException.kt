package com.klewerro.githubusers.core.data.error

class GithubApiException(
    val errorType: GithubApiErrorType,
    val originalException: Throwable? = null
) : Exception(
    "An error occurred during request: $errorType",
    originalException
)

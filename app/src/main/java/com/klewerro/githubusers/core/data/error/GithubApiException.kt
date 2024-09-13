package com.klewerro.githubusers.core.data.error

class GithubApiException(
    val errorType: GithubApiErrorType,
    val originalException: Throwable? = null,
    override val message: String = "An error occurred during request: $errorType",
    val additionalInformation: String? = null
) : Exception(
    message,
    originalException
)

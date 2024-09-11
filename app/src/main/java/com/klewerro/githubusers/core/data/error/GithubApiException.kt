package com.klewerro.githubusers.core.data.error

class GithubApiException(val error: GithubApiErrorType) : Exception(
    "An error occurred during request: $error"
)

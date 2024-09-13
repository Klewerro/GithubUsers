package com.klewerro.githubusers.users.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.klewerro.githubusers.R
import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException

object GithubApiExceptionMessageConverter {
    fun GithubApiException.convertGithubApiExceptionToUserMessage(context: Context): String =
        when (this.errorType) {
            GithubApiErrorType.SERVICE_UNAVAILABLE -> context.getString(
                R.string.missing_internet_connection
            )

            GithubApiErrorType.CLIENT_ERROR -> context.getString(R.string.client_error_description)
            GithubApiErrorType.SERVER_ERROR -> context.getString(R.string.server_error_description)
            GithubApiErrorType.SERIALIZATION_ERROR ->
                context.getString(R.string.serialization_error_description)

            GithubApiErrorType.RATE_LIMIT_EXCEEDED ->
                context.getString(R.string.rate_limit_exceeded_description)

            GithubApiErrorType.UNPROCESSABLE_QUERY ->
                context.getString(R.string.unprocessable_query_description)

            GithubApiErrorType.UNKNOWN_ERROR ->
                context.getString(R.string.unknown_error_please_contact_with_the_support)
        }

    @Composable
    fun GithubApiException.convertGithubApiExceptionToUserMessage(): String =
        when (this.errorType) {
            GithubApiErrorType.SERVICE_UNAVAILABLE -> stringResource(
                R.string.missing_internet_connection
            )

            GithubApiErrorType.CLIENT_ERROR -> stringResource(R.string.client_error_description)
            GithubApiErrorType.SERVER_ERROR -> stringResource(R.string.server_error_description)
            GithubApiErrorType.SERIALIZATION_ERROR ->
                stringResource(R.string.serialization_error_description)

            GithubApiErrorType.RATE_LIMIT_EXCEEDED ->
                stringResource(R.string.rate_limit_exceeded_description)

            GithubApiErrorType.UNPROCESSABLE_QUERY ->
                stringResource(R.string.unprocessable_query_description)

            GithubApiErrorType.UNKNOWN_ERROR ->
                stringResource(R.string.unknown_error_please_contact_with_the_support)
        }
}

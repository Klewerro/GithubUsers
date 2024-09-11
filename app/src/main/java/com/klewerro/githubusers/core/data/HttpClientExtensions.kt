package com.klewerro.githubusers.core.data

import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import java.io.IOException

suspend inline fun <reified T> HttpResponse.runThrowing(): T {
    val result =
        try {
            this.body<T>()
        } catch (e: IOException) {
            throw GithubApiException(GithubApiErrorType.SERVICE_UNAVAILABLE)
        } catch (e: NoTransformationFoundException) {
            throw GithubApiException(GithubApiErrorType.SERIALIZATION_ERROR)
        } catch (e: Exception) {
            throw GithubApiException(GithubApiErrorType.SERVER_ERROR)
        }

    when (this.status.value) {
        in 200..299 -> Unit
        500 -> throw GithubApiException(GithubApiErrorType.SERVER_ERROR)
        in 400..499 -> throw GithubApiException(GithubApiErrorType.CLIENT_ERROR)
        else -> throw GithubApiException(GithubApiErrorType.UNKNOWN_ERROR)
    }

    return result
}

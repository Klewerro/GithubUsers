package com.klewerro.githubusers.core.data

import com.klewerro.githubusers.core.data.error.GithubApiErrorType
import com.klewerro.githubusers.core.data.error.GithubApiException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException
import timber.log.Timber
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

suspend inline fun <reified T> runRequestThrowing(httpCallFunction: () -> HttpResponse): T {
    val result = try {
        val httpResponse = httpCallFunction()

        val remainingRateHeader = httpResponse.headers[ApiConstants.HEADER_REMAINING_RATE]
        Timber.d("remainingRateHeader: $remainingRateHeader")

        when (httpResponse.status.value) {
            in 200..299 -> Unit
            500 -> throw GithubApiException(GithubApiErrorType.SERVER_ERROR)
            403 -> {
                httpResponse.headers[ApiConstants.HEADER_RATE_LIMIT_RESET_TIME]?.let { rateLimitReset ->
                    Timber.d("rate limit reset time long: $rateLimitReset")
                    val rateLimitResetLong = rateLimitReset.toLong()
                    val dateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(rateLimitResetLong),
                        TimeZone.getDefault().toZoneId()
                    )
                    throw GithubApiException(
                        errorType = GithubApiErrorType.RATE_LIMIT_EXCEEDED,
                        additionalInformation = dateTime.toLocalTime().format(
                            DateTimeFormatter.ofPattern("HH:mm:ss")
                        )
                    )
                } ?: run {
                    throw GithubApiException(GithubApiErrorType.RATE_LIMIT_EXCEEDED)
                }
            }
            422 -> throw GithubApiException(GithubApiErrorType.UNPROCESSABLE_QUERY)
            in 400..499 -> throw GithubApiException(GithubApiErrorType.CLIENT_ERROR)
            else -> throw GithubApiException(GithubApiErrorType.UNKNOWN_ERROR)
        }

        httpResponse.body<T>()
    } catch (e: IOException) {
        Timber.e(e)
        throw GithubApiException(GithubApiErrorType.SERVICE_UNAVAILABLE, e)
    } catch (e: NoTransformationFoundException) {
        Timber.e(e)
        throw GithubApiException(GithubApiErrorType.SERIALIZATION_ERROR, e)
    } catch (e: JsonConvertException) {
        Timber.e(e)
        throw GithubApiException(GithubApiErrorType.SERIALIZATION_ERROR, e)
    } catch (e: GithubApiException) {
        throw e
    } catch (e: Exception) {
        Timber.e(e)
        throw GithubApiException(GithubApiErrorType.UNKNOWN_ERROR, e)
    }

    return result
}

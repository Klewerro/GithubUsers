package com.klewerro.githubusers.core.data

object ApiConstants {
    const val BASE_URL = "https://api.github.com/"

    const val SEARCH_FOR_USERS_URL = BASE_URL + "search/users"
    const val PARAM_PER_PAGE = "per_page"
    const val PARAM_PAGE = "page"
    const val PARAM_QUERY = "q"
    const val HEADER_REMAINING_RATE = "x-ratelimit-remaining"
    const val HEADER_RATE_LIMIT_RESET_TIME = "x-ratelimit-reset"
    fun userDetails(login: String) = BASE_URL + "users/$login"

    fun userReposUrl(login: String) = BASE_URL + "users/$login/repos"
    const val PARAM_SORT = "sort"
    const val VALUE_SORT_UPDATED = "updated"
}

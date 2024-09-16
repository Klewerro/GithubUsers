package com.klewerro.githubusers.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Drops last sign (z, meaning UTC time) from ISO 8601 date time text and convert it to
 * Java LocalDateTime.
 */
fun convertIso8601DateToLocalDateTime(iso8601DateTimeText: String): String {
    val date = LocalDateTime.parse(iso8601DateTimeText.dropLast(1))
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")
    val formattedDate = date.format(formatter)
    return formattedDate
}

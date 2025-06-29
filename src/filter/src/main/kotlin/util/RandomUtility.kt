package org.asr.example.util

import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

private const val chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM 1234567890"

fun randomString(length: Int = 8): String {
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}

fun randomStrings(
    count: Int = 100,
    startLength: Int = 6,
    endLength: Int = 12
): Array<String> {
    return Array(count) {
        randomString(random.nextInt(startLength, endLength))
    }
}
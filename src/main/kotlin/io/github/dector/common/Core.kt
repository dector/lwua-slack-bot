package io.github.dector.common

inline fun <reified T> tryOrNull(producer: () -> T): T? {
    return try {
        producer()
    } catch (e: Throwable) {
        null
    }
}
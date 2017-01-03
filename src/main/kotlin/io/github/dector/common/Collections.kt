package io.github.dector.common

fun <T> List<T>.firstOrElse(fallbackProducer: () -> T): T {
    return if (isEmpty()) fallbackProducer() else this[0]
}
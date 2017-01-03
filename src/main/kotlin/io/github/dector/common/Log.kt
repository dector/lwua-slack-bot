package io.github.dector.common

fun String.log(tag: String = "") {
    val msg = if (tag.isNotEmpty()) "[$tag] $this" else this
    println(msg)
}
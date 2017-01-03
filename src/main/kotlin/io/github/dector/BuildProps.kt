package io.github.dector

import io.github.dector.common.tryOrNull
import java.io.FileInputStream
import java.util.*

class BuildProps {

    val props = Properties()

    val webhookUrl: String
    get() = props.getProperty("SLACK_WEBHOOK_URL") ?: ""

    private fun load(file: String) {
        val inputStream = tryOrNull { FileInputStream(file) }

        try {
            props.load(inputStream)
        } finally {
            inputStream?.close()
        }
    }

    companion object {

        val default: BuildProps by lazy {
            BuildProps().apply { load("secure.properties") }
        }
    }
}
package io.github.dector.slack

import com.squareup.moshi.Moshi
import io.github.dector.common.log
import io.github.dector.slack.rest.AttachmentColorAdapter
import io.github.dector.slack.rest.Message
import io.github.dector.slack.rest.SlackWebHookAPI
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SlackAPI(webhookUrl: String, val debug: Boolean = false) {

    private val retrofit = Retrofit.Builder()
            .baseUrl(webhookUrl)
            .addConverterFactory(createConverterFactory())
            .build()

    private val webhookAPI = retrofit.create(
            SlackWebHookAPI::class.java)

    fun sendMessage(message: Message) {
        "Sending message: $message".log()

        if (debug) return

        val response = webhookAPI.sendMessage(message)
                .execute()

        if (response.isSuccessful) {
            "Received response: ${response.body()}".log()
        } else {
            "Error happened (${response.code()}): ${response.errorBody().string()}".log()
        }
    }

    private fun String.log() {
        this.log("SlackAPI")
    }

    private fun createConverterFactory() = Moshi.Builder()
            .add(AttachmentColorAdapter())
            .build().let { MoshiConverterFactory.create(it) }
}
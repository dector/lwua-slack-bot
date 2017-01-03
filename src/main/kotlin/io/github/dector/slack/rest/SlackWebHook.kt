package io.github.dector.slack.rest

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SlackWebHookAPI {

    @POST()
    @Headers("Content-Type: application/json")
    fun sendMessage(@Body message: Message): Call<Void>
}

data class Message(
        @Json(name = "icon_emoji") val emojiIcon: String,
        val username: String,
        val text: String,
        val attachments: List<Attachment>,
        val channel: String = "",
        val unfurl_links: Boolean = false
)

enum class AttachmentColor(val value: String) {
    GOOD("good"), WARNING("warning"), DANGER("danger"), NONE("")
}

data class Attachment(
        val color: AttachmentColor,
        @Json(name = "author_name") val authorName: String,
        @Json(name = "author_link") val authorLink: String,
        val text: String,
        val footer: String,
        @Json(name = "footer_icon") val footerIcon: String,
        @Json(name = "image_url") val imageUrl: String,
        @Json(name = "ts") val timestamp: String
)

class AttachmentColorAdapter {

    @ToJson
    fun toJson(color: AttachmentColor) = color.value

    @FromJson
    fun fromJson(value: String) = AttachmentColor.values()
            .firstOrNull { it.value == value }
            .let { it ?: AttachmentColor.NONE }
}
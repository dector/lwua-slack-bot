package io.github.dector

import io.github.dector.common.log
import io.github.dector.meetup.MeetupAPI
import io.github.dector.meetup.rest.Event
import io.github.dector.slack.SlackAPI
import io.github.dector.slack.rest.Attachment
import io.github.dector.slack.rest.AttachmentColor
import io.github.dector.slack.rest.Message
import java.time.ZoneOffset

fun main(vararg args: String) {
    if (!checkPreconditions()) {
        return
    }

    val event = MeetupAPI("LessWrong-Kyiv")
            .upcomingEvents()
            .firstOrNull()

    if (event == null) {
        "There is no events in schedule".log()
        return
    }

    SlackAPI(webHookUrl = getWebhookUrl(), debug = false)
            .sendMessage(buildSlackMessage(event))
}

fun checkPreconditions(): Boolean {
    if (getWebhookUrl().isEmpty()) {
        "ERROR! Slack WebHook Url shouldn't be empty. Checkout documentation first: https://api.slack.com/incoming-webhooks".log()
        return false
    }

    return true
}

fun getWebhookUrl(): String = BuildProps.default.webhookUrl

fun buildSlackMessage(event: Event) = Message(
        emojiIcon = ":meetup:",
        username = "Meetup.com",
        text = "<!channel> Схоже, є привід зустрітися! Деталі <${event.link}|тут>",
        attachments = listOf(
                Attachment(
                        color = AttachmentColor.GOOD,
                        authorName = event.name,
                        authorLink = event.link,
                        text = event.plainTextDescription,
                        footer = "<https://www.google.com.ua/maps/place/${event.venue.lat}%20${event.venue.lon}|${event.venue.name}>",
                        footerIcon = "http://i.imgur.com/p4hmlZh.png",
                        imageUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=17&size=400x200&markers=color:yellowred|${event.venue.lat},${event.venue.lon}",
                        timestamp = event.time.toEpochSecond(ZoneOffset.UTC).toString()
                )
        )
)
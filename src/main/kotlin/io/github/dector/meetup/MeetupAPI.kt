package io.github.dector.meetup

import com.squareup.moshi.Moshi
import io.github.dector.common.log
import io.github.dector.meetup.rest.Event
import io.github.dector.meetup.rest.MeetupEventsAPI
import io.github.dector.meetup.rest.TimeAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MeetupAPI(val groupName: String) {

    private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.meetup.com")
            .addConverterFactory(createConverterFactory())
            .build()

    private val meetupEventsAPI = retrofit.create(
            MeetupEventsAPI::class.java)

    fun upcomingEvents(): List<Event> {
        return getEvents(status = "upcoming", fields = "plain_text_description")
    }

    fun pastEvents(): List<Event> {
        return getEvents(status = "past", fields = "plain_text_description")
    }

    fun getEvents(status: String = "", fields: String = ""): List<Event> {
        "Requesting $status events".log()

        val response = meetupEventsAPI.getEvents(
                groupName = groupName,
                status = status,
                fields = fields).execute()

        if (response.isSuccessful) {
            "Received response: ${response.body()}".log()

            return response.body()
        } else {
            "Error happened (${response.code()}): ${response.errorBody().string()}".log()

            return emptyList()
        }
    }

    private fun String.log() {
        this.log("MeetupAPI")
    }

    private fun createConverterFactory() = Moshi.Builder()
            .add(TimeAdapter())
            .build().let { MoshiConverterFactory.create(it) }
}
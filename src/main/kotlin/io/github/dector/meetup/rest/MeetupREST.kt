package io.github.dector.meetup.rest

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.ZoneOffset

interface MeetupEventsAPI {

    /**
     * https://www.meetup.com/meetup_api/docs/:urlname/events/
     */
    @GET("/{urlname}/events")
    fun getEvents(@Path("urlname") groupName: String,
                  @Query("status") status: String = "",
                  @Query("fields") fields: String = ""): Call<List<Event>>
}

data class Event(
        val name: String,
        val time: LocalDateTime,
        val venue: Venue,
        val link: String,
        @Json(name = "plain_text_description") val plainTextDescription: String
)

data class Venue(
        val name: String,
        val lat: Double,
        val lon: Double
)

class TimeAdapter {

    @ToJson
    fun toJson(time: LocalDateTime) = time.toEpochSecond(ZoneOffset.UTC) * 1000

    @FromJson
    fun fromJson(timestampMs: Long) = LocalDateTime.ofEpochSecond(timestampMs / 1000, 0, ZoneOffset.UTC)
}
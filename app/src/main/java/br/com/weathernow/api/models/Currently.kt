package br.com.weathernow.api.models

import com.squareup.moshi.Json

data class Currently(
    @Json(name = "apparentTemperature")
    val apparentTemperature: Double?,
    @Json(name = "humidity")
    val humidity: Double?,
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "precipProbability")
    val precipProbability: Double?,
    @Json(name = "summary")
    val summary: String?,
    @Json(name = "temperature")
    val temperature: Double?,
    @Json(name = "windSpeed")
    val windSpeed: Double?
)

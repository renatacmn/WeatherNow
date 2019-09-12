package br.com.weathernow.api.models

import com.squareup.moshi.Json

data class Currently(
    @Json(name = "apparentTemperature")
    val apparentTemperature: Double?,
    @Json(name = "summary")
    val summary: String?,
    @Json(name = "temperature")
    val temperature: Double?
)

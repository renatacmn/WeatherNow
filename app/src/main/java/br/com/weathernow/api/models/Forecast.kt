package br.com.weathernow.api.models

import com.squareup.moshi.Json

data class Forecast(
    @Json(name = "currently")
    val currently: Currently?,
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?,
    @Json(name = "timezone")
    val timezone: String?
)

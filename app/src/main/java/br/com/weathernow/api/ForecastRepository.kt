package br.com.weathernow.api

import br.com.weathernow.api.models.Forecast

class ForecastRepository(
    private val api: ForecastApi
) {

    suspend fun getForecast(
        latitude: Double,
        longitude: Double
    ): Forecast {
        return api.getForecast(latitude, longitude)
    }

}

package br.com.weathernow.util

import br.com.weathernow.api.models.Currently
import br.com.weathernow.api.models.Forecast

class TestUtil {

    fun getFakeForecast(
        latitude: Double = TEST_LATITUDE,
        longitude: Double = TEST_LONGITUDE
    ): Forecast {
        return Forecast(
            currently = getFakeCurrently(),
            latitude = latitude,
            longitude = longitude,
            timezone = TEST_TIMEZONE
        )
    }

    private fun getFakeCurrently(): Currently {
        return Currently(
            apparentTemperature = TEST_TEMPERATURE,
            summary = TEST_SUMMARY,
            temperature = TEST_TEMPERATURE
        )
    }

    companion object {
        const val TEST_LATITUDE = 59.333718
        const val TEST_LONGITUDE = 18.036048
        private const val TEST_TIMEZONE = "Europe/Stockholm"
        private const val TEST_TEMPERATURE = 60.0
        private const val TEST_SUMMARY = "Mostly Cloudy"
    }

}
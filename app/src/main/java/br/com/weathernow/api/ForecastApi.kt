package br.com.weathernow.api

import br.com.weathernow.api.models.Forecast
import retrofit2.http.GET
import retrofit2.http.Path

interface ForecastApi {

    @GET("forecast/$API_KEY/{latitude},{longitude}")
    suspend fun getCurrentWeather(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Forecast

    companion object {
        private const val API_KEY = "2bb07c3bece89caf533ac9a5d23d8417"
    }

}

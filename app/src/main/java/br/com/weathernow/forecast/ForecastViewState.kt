package br.com.weathernow.forecast

import br.com.weathernow.api.models.Forecast

sealed class ForecastViewState {

    object Loading : ForecastViewState()

    class Success(val forecast: Forecast) : ForecastViewState()

    class Error(val exception: Exception) : ForecastViewState()

}

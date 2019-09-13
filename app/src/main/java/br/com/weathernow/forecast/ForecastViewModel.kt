package br.com.weathernow.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.weathernow.api.ForecastRepository
import br.com.weathernow.forecast.ForecastViewState.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val repository: ForecastRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val forecastViewStateLive = MutableLiveData<ForecastViewState>()

    fun getForecastViewState(): LiveData<ForecastViewState> = forecastViewStateLive

    fun getForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch(coroutineDispatcher) {
            forecastViewStateLive.postValue(Loading)
            try {
                val forecast = repository.getForecast(latitude, longitude)
                forecastViewStateLive.postValue(Success(forecast))
            } catch (exception: Exception) {
                forecastViewStateLive.postValue(Error(exception))
            }
        }
    }

}

package br.com.weathernow.forecast

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.weathernow.R
import br.com.weathernow.api.models.Forecast
import br.com.weathernow.forecast.ForecastViewState.*
import br.com.weathernow.location.LocationActivity

class ForecastActivity : LocationActivity(), LocationActivity.LatLongListener {

    private lateinit var viewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        init()
        checkPermissionAndGetLastKnownLocation(this)
    }

    // LocationActivity overrides

    override fun onLatLongReady(latitude: Double, longitude: Double) {
        viewModel.getForecast(latitude, longitude)
    }

    override fun onLastKnownLocationError() {
        showAlertDialog(
            getString(R.string.last_known_location_null_dialog_title),
            getString(R.string.last_known_location_null_dialog_message)
        )
    }

    override fun showGooglePlayServicesCanceledUi() {
        Toast.makeText(this, "showGooglePlayServicesCanceledUi", Toast.LENGTH_SHORT).show()
    }

    override fun showLocationPermissionDeniedUi() {
        Toast.makeText(this, "showLocationPermissionDeniedUi", Toast.LENGTH_SHORT).show()
    }

    // Private methods

    private fun init() {
        initializeViewModel()
        initializeObserver()
        initializeViewComponents()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java)
    }

    private fun initializeObserver() {
        viewModel.getForecastViewState().observe(this, Observer(this::updateUi))
    }

    private fun initializeViewComponents() {

    }

    private fun updateUi(viewState: ForecastViewState) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> showForecast(viewState.forecast)
            is Error -> showError(viewState.exception)
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "showLoading", Toast.LENGTH_SHORT).show()
    }

    private fun showForecast(forecast: Forecast) {
        Toast.makeText(this, "showForecast", Toast.LENGTH_SHORT).show()
    }

    private fun showError(exception: Exception) {
        Toast.makeText(this, "showError", Toast.LENGTH_SHORT).show()
    }

}

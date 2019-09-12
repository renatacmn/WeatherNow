package br.com.weathernow.forecast

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.weathernow.R
import br.com.weathernow.api.models.Forecast
import br.com.weathernow.forecast.ForecastViewState.*
import br.com.weathernow.location.LocationActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.activity_weather_state_error.*
import kotlinx.android.synthetic.main.activity_weather_state_success.*

class ForecastActivity : LocationActivity(), LocationActivity.LatLongListener {

    private lateinit var viewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        init()
    }

    override fun onResume() {
        super.onResume()
        checkPermissionAndGetLastKnownLocation(this)
    }

    // LocationActivity overrides

    override fun onLatLongReady(latitude: Double, longitude: Double) {
        if (isNetworkAvailable()) {
            viewModel.getForecast(latitude, longitude)
        } else {
            showError(getString(R.string.network_not_available_message))
            { checkPermissionAndGetLastKnownLocation(this) }
        }
    }

    override fun onLastKnownLocationError() {
        showAlertDialog(
            getString(R.string.last_known_location_null_dialog_title),
            getString(R.string.last_known_location_null_dialog_message)
        )
    }

    override fun showGooglePlayServicesCanceledUi() {
        showError(getString(R.string.google_play_services_fix_canceled_message))
        { checkGooglePlayServices() }
    }

    override fun showLocationPermissionDeniedUi() {
        showError(getString(R.string.location_permission_rationale_dialog_message))
        { checkPermissionAndGetLastKnownLocation(this) }
    }

    override fun showLocationPermissionPermanentlyDeniedUi() {
        showError(getString(R.string.location_permission_rationale_dialog_message))
        { goToSettings() }
    }

    // Private methods

    private fun init() {
        initializeViewModel()
        initializeObserver()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java)
    }

    private fun initializeObserver() {
        viewModel.getForecastViewState().observe(this, Observer(this::updateUi))
    }

    private fun updateUi(viewState: ForecastViewState) {
        when (viewState) {
            is Loading -> showLoading()
            is Success -> showForecast(viewState.forecast)
            is Error -> showError(viewState.exception)
        }
    }

    private fun showLoading() {
        forecastLayoutStateLoading.visibility = View.VISIBLE
        forecastLayoutStateSuccess.visibility = View.GONE
        forecastLayoutStateError.visibility = View.GONE
    }

    private fun showForecast(forecast: Forecast) {
        setForecastInfo(forecast)
        forecastLayoutStateLoading.visibility = View.GONE
        forecastLayoutStateSuccess.visibility = View.VISIBLE
        forecastLayoutStateError.visibility = View.GONE
    }

    private fun setForecastInfo(forecast: Forecast) {
        textTemperature.text =
            getString(R.string.forecast_temperature, forecast.currently?.temperature)
        textApparentTemperature.text = getString(
            R.string.forecast_apparent_temperature, forecast.currently?.apparentTemperature
        )
        textSummary.text = forecast.currently?.summary
    }

    private fun showError(exception: Exception) {
        showError(exception.message) { checkPermissionAndGetLastKnownLocation(this) }
    }

    private fun showError(message: String?, tryAgainAction: () -> Unit) {
        forecastLayoutStateLoading.visibility = View.GONE
        forecastLayoutStateSuccess.visibility = View.GONE
        forecastLayoutStateError.visibility = View.VISIBLE
        setErrorInfo(message, tryAgainAction)
    }

    private fun setErrorInfo(message: String?, tryAgainAction: () -> Unit) {
        textErrorMessage.text = message
        icTryAgain.setOnClickListener { tryAgainAction() }
    }

}

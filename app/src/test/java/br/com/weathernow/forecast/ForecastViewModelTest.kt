package br.com.weathernow.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.weathernow.api.ForecastRepository
import br.com.weathernow.util.TestUtil
import br.com.weathernow.util.TestUtil.Companion.TEST_LATITUDE
import br.com.weathernow.util.TestUtil.Companion.TEST_LONGITUDE
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ForecastViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ForecastRepository

    private lateinit var forecastViewModel: ForecastViewModel

    @Before
    @ExperimentalCoroutinesApi
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.forecastViewModel = ForecastViewModel(repository, Dispatchers.Unconfined)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun getForecast_onSuccess_shouldUpdateViewState() {
        val fakeForecast = TestUtil().getFakeForecast()

        // Mock API response
        runBlocking {
            whenever(
                repository.getForecast(
                    ArgumentMatchers.anyDouble(),
                    ArgumentMatchers.anyDouble()
                )
            ).thenReturn(fakeForecast)
        }

        // Invoke
        forecastViewModel.getForecast(TEST_LATITUDE, TEST_LONGITUDE)

        // Verify
        assertNotNull(forecastViewModel.getForecastViewState())
        assertTrue(forecastViewModel.getForecastViewState().value is ForecastViewState.Success)
        assertEquals(
            (forecastViewModel.getForecastViewState().value as ForecastViewState.Success).forecast,
            fakeForecast
        )
    }

    @Test
    fun getForecast_onException_shouldUpdateViewState() {
        val exception = RuntimeException("test")
        // Mock API response
        runBlocking {
            whenever(
                repository.getForecast(
                    ArgumentMatchers.anyDouble(),
                    ArgumentMatchers.anyDouble()
                )
            ).thenThrow(exception)
        }

        // Invoke
        forecastViewModel.getForecast(TEST_LATITUDE, TEST_LONGITUDE)

        // Verify
        assertNotNull(forecastViewModel.getForecastViewState())
        assertTrue(forecastViewModel.getForecastViewState().value is ForecastViewState.Error)
        assertEquals(
            (forecastViewModel.getForecastViewState().value as ForecastViewState.Error).exception,
            exception
        )
    }

}

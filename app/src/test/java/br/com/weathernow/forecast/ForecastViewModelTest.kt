package br.com.weathernow.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.weathernow.api.ForecastRepository
import br.com.weathernow.util.TestUtil
import br.com.weathernow.util.TestUtil.Companion.TEST_LATITUDE
import br.com.weathernow.util.TestUtil.Companion.TEST_LONGITUDE
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class ForecastViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ForecastRepository

    private lateinit var forecastViewModel: ForecastViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.forecastViewModel = ForecastViewModel(repository)
    }

    @Test
    fun getForecastViewState_shouldReturnForecastViewStateLive() {
        val fakeForecast = TestUtil().getFakeForecast()
        val expected = ForecastViewState.Success(fakeForecast)

        // Set value
        forecastViewModel.forecastViewStateLive.value = expected

        // Verify
        assertEquals(
            expected,
            forecastViewModel.getForecastViewState().value
        )
    }

    @Test
    fun getForecast_onLoading_shouldUpdateViewState() {
        // TODO
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

        // Attach fake observer
        val observer = mock(Observer::class.java) as Observer<ForecastViewState>
        forecastViewModel.getForecastViewState().observeForever(observer)

        // Invoke
        forecastViewModel.getForecast(TEST_LATITUDE, TEST_LONGITUDE)

        // Verify
        assertNotNull(forecastViewModel.getForecastViewState())
        Thread.sleep(100)
        assertTrue(forecastViewModel.getForecastViewState().value is ForecastViewState)
        assertEquals(
            (forecastViewModel.getForecastViewState().value as ForecastViewState.Success).forecast,
            fakeForecast
        )
    }

    @Test
    fun getForecast_onException_shouldUpdateViewState() {
        // TODO
    }

}

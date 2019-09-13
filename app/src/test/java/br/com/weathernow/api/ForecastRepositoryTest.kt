package br.com.weathernow.api

import br.com.weathernow.util.TestUtil.Companion.TEST_LATITUDE
import br.com.weathernow.util.TestUtil.Companion.TEST_LONGITUDE
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ForecastRepositoryTest {

    @Mock
    private lateinit var api: ForecastApi

    private lateinit var repository: ForecastRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = ForecastRepository(api)
    }

    @Test
    fun getForecast() {
        runBlocking {
            repository.getForecast(TEST_LATITUDE, TEST_LONGITUDE)
            verify(api, times(1)).getForecast(TEST_LATITUDE, TEST_LONGITUDE)
        }
    }

}

package br.com.weathernow.api

import br.com.weathernow.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkUtil {

    val api: ForecastApi = getRetrofit().create(ForecastApi::class.java)

    // Private methods

    private fun getRetrofit() = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getOkHttpClient())
            .build()

    private fun getOkHttpClient() = OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .build()

    private fun getLoggingInterceptor() = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }

}

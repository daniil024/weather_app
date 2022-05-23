package com.example.yandexweatherapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object OpenWeatherRetrofit {

    private const val BASE_URL = "http://api.openweathermap.org/"
    private const val API_KEY = "76d3e0b6814244b808741463ae514383"

    private val interceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }


    private val apiInterceptor = Interceptor {
        val originalRequest = it.request()
        val newHttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("appid", API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()
        it.proceed(newRequest)
    }

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val contentType = "application/json".toMediaType()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(apiInterceptor)
        .addInterceptor(interceptor)
        .build()

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val openWeatherService: OpenWeatherService = retrofit.create()
}
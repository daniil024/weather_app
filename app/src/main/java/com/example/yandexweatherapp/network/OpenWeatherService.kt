package com.example.yandexweatherapp.network

import com.example.yandexweatherapp.models.OneApiCallWeatherDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("data/2.5/onecall?units=metric")
    suspend fun getOneCallWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String
    ): OneApiCallWeatherDTO
}
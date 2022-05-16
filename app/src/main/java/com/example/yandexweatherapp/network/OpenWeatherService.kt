package com.example.yandexweatherapp.network

import com.example.yandexweatherapp.models.OneApiCallWeather
import retrofit2.http.GET

interface OpenWeatherService {

    @GET("data/2.5/onecall?lat={lat}&lon={lon}&lang={lang}")
    suspend fun getOneCallWeather():OneApiCallWeather
}
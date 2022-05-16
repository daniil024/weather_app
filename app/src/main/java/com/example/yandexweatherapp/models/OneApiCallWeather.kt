package com.example.yandexweatherapp.models

import kotlinx.serialization.Serializable

@Serializable
data class OneApiCallWeather(
    val lat: String,
    val lon:String,
    val timezone:String,
    val timezone_offset:
)

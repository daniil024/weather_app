package com.example.yandexweatherapp.models

import kotlinx.serialization.Serializable

@Serializable
data class OneApiCallWeatherDTO(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: CurrentDTO,
    val hourly: List<HourlyDTO>,
    val daily: List<DailyDTO>
)


@Serializable
data class CurrentDTO(

    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val clouds: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>
)

@Serializable
data class WeatherDTO(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class HourlyDTO(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>
)

@Serializable
data class DailyDTO(
    val dt: Long,
    val temp: DailyTempDTO,
    val feels_like: DailyTempDTO,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>
)

sealed class DailyHourlyAdapter

// Class to map our DailyDTO to four objects with temp as Double
data class DailyHourly(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>,
    val type: DailyHourlyType,
    val dayTime: DayTime?
) {
    constructor(
        dt: Long,
        temp: Double,
        feels_like: Double,
        pressure: Int,
        humidity: Int,
        wind_speed: Double,
        weather: List<WeatherDTO>,
        type: DailyHourlyType
    ) : this(dt, temp, feels_like, pressure, humidity, wind_speed, weather, type, DayTime.DEFAULT)
}

enum class DailyHourlyType {
    DAILY,
    HOURLY
}

enum class DayTime {
    MORNING,
    DAY,
    EVENING,
    NIGHT,
    DEFAULT
}


@Serializable
data class DailyTempDTO(
    val morn: Double,
    val day: Double,
    val eve: Double,
    val night: Double
)

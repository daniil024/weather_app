package com.example.yandexweatherapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yandexweatherapp.models.CurrentDTO
import com.example.yandexweatherapp.models.DailyDTO
import com.example.yandexweatherapp.models.HourlyDTO

@Entity(tableName = "one_api_call")
data class OneApiCallWeatherEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "timezone") val timezone: String,
    @ColumnInfo(name = "timezone_offset") val timezone_offset: Int,
    @ColumnInfo(name = "current") val current: CurrentDTO,
    @ColumnInfo(name = "hourly") val hourly: List<HourlyDTO>,
    @ColumnInfo(name = "daily") val daily: List<DailyDTO>
)

@Entity
data class CurrentEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "dt") val dt: Long,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "feels_like") val feels_like: Double,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "clouds") val clouds: Int,
    @ColumnInfo(name = "wind_speed") val wind_speed: Double,
    @ColumnInfo(name = "weather") val weather: List<WeatherEntity>
)

@Entity
data class WeatherEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "main") val main: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "icon") val icon: String
)

@Entity
data class HourlyEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "dt") val dt: Long,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "feels_like") val feels_like: Double,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "wind_speed") val wind_speed: Double,
    @ColumnInfo(name = "weather") val weather: List<WeatherEntity>
)

@Entity
data class DailyEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "dt") val dt: Long,
    @ColumnInfo(name = "temp") val temp: DailyTempEntity,
    @ColumnInfo(name = "feels_like") val feels_like: DailyTempEntity,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "wind_speed") val wind_speed: Double,
    @ColumnInfo(name = "weather") val weather: List<WeatherEntity>
)

@Entity
data class DailyTempEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "morn") val morn: Double,
    @ColumnInfo(name = "day") val day: Double,
    @ColumnInfo(name = "eve") val eve: Double,
    @ColumnInfo(name = "night") val night: Double
)


package com.example.yandexweatherapp.models

import androidx.room.*
import com.example.yandexweatherapp.room.type_converters.CurrentTypeConverter
import com.example.yandexweatherapp.room.type_converters.DailyTypeConverter
import com.example.yandexweatherapp.room.type_converters.HourlyTypeConverter
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "one_api_call", indices = [Index(value = ["timezone"], unique = true)])
data class OneApiCallWeatherDTO(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "timezone") val timezone: String,
    @ColumnInfo(name = "timezone_offset") val timezone_offset: Int,
    @ColumnInfo(name = "current")
    @TypeConverters(CurrentTypeConverter::class)
    val current: CurrentDTO,
    @ColumnInfo(name = "hourly")
    @TypeConverters(HourlyTypeConverter::class)
    val hourly: List<HourlyDTO>,
    @ColumnInfo(name = "daily")
    @TypeConverters(DailyTypeConverter::class)
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

sealed class DailyHourlyAdapter

@Serializable
data class HourlyDTO(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>
) : DailyHourlyAdapter()

@Serializable
data class DailyDTO(
    val dt: Long,
    val temp: DailyTempDTO,
    val feels_like: DailyTempDTO,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>
) : DailyHourlyAdapter()

// Class to map our DailyDTO to four objects with temp as Double
data class DailyHourly(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<WeatherDTO>
)

enum class DailyHourlyEnum(val type:String){
    DAILY("Неделя"),
    HOURLY("Сегодня")
}

@Serializable
data class DailyTempDTO(
    val morn: Double,
    val day: Double,
    val eve: Double,
    val night: Double
)

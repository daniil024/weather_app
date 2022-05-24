package com.example.yandexweatherapp.utils

import com.example.yandexweatherapp.models.*

object Mapper {

    public fun mapDailyDTO2DailyHourly(dailyDTO: List<DailyDTO>): ArrayList<DailyHourly> {
        val result: ArrayList<DailyHourly> = ArrayList();
        for (d in dailyDTO) {
            result.add(
                DailyHourly(
                    d.dt, d.temp.morn, d.feels_like.morn,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0]),
                    DailyHourlyType.DAILY,
                    DayTime.MORNING
                )
            )
            result.add(
                DailyHourly(
                    d.dt, d.temp.day, d.feels_like.day,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0]),
                    DailyHourlyType.DAILY, DayTime.DAY
                )
            )
            result.add(
                DailyHourly(
                    d.dt, d.temp.eve, d.feels_like.eve,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0]),
                    DailyHourlyType.DAILY, DayTime.EVENING
                )
            )
            result.add(
                DailyHourly(
                    d.dt, d.temp.night, d.feels_like.night,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0]),
                    DailyHourlyType.DAILY, DayTime.NIGHT
                )
            )
        }

        return result
    }

    public fun mapHourlyDTO2DailyHourly(dailyDTO: List<HourlyDTO>): List<DailyHourly> {
        return dailyDTO.map { d ->
            DailyHourly(
                d.dt,
                d.temp,
                d.feels_like,
                d.pressure,
                d.humidity,
                d.wind_speed,
                d.weather,
                DailyHourlyType.HOURLY
            )
        }
    }
}
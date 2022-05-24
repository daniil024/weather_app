package com.example.yandexweatherapp.utils

import com.example.yandexweatherapp.models.DailyDTO
import com.example.yandexweatherapp.models.DailyHourly
import com.example.yandexweatherapp.models.HourlyDTO

object Mapper {

    public fun mapDailyDTO2DailyHourly(dailyDTO: List<DailyDTO>): ArrayList<DailyHourly> {
        val result: ArrayList<DailyHourly> = ArrayList();
        for (d in dailyDTO) {
            result.add(
                DailyHourly(
                    d.dt, d.temp.morn, d.feels_like.morn,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0])
                )
            )
            result.add(
                DailyHourly(
                    d.dt, d.temp.day, d.feels_like.day,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0])
                )
            )
            result.add(
                DailyHourly(
                    d.dt, d.temp.eve, d.feels_like.eve,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0])
                )
            )
            result.add(
                DailyHourly(
                    d.dt, d.temp.night, d.feels_like.night,
                    d.pressure, d.humidity, d.wind_speed, listOf(d.weather[0])
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
                d.weather
            )
        }
    }
}
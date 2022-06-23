package com.example.yandexweatherapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yandexweatherapp.models.OneApiCallWeatherDTO
import com.example.yandexweatherapp.room.entities.OneApiCallWeatherEntity

@Dao
interface OneApiCallWeatherDao {

    @Query("SELECT * FROM one_api_call WHERE lat LIKE :lat AND lon LIKE :lon")
    fun getWeatherAtPlace(lat: Double, lon: Double): OneApiCallWeatherDTO?

    @Query("SELECT * FROM one_api_call WHERE timezone LIKE :timezone")
    fun getWeatherByTimeZone(timezone: String): OneApiCallWeatherDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherAtPlace(data: OneApiCallWeatherDTO)
}
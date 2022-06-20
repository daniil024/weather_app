package com.example.yandexweatherapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yandexweatherapp.room.entities.OneApiCallWeatherEntity

@Dao
interface OneApiCallWeatherDao {

    @Query("SELECT * FROM one_api_call LIMIT 1")  //WHERE lat LIKE :lat AND lon LIKE :lon
    fun getWeatherAtPlace(lat: Double, lon: Double): OneApiCallWeatherEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherAtPlace(data: OneApiCallWeatherEntity)
}
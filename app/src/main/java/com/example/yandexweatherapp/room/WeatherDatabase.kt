package com.example.yandexweatherapp.room

import android.content.Context
import androidx.room.*
import com.example.yandexweatherapp.room.entities.OneApiCallWeatherEntity

@Database(entities = [OneApiCallWeatherEntity::class], version = 1)
@TypeConverters(DataTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun oneApiCallWeatherDao(): OneApiCallWeatherDao

    companion object {
        var INSTANCE: WeatherDatabase? = null

        fun getWeatherDatabase(context: Context): WeatherDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "one_api_call"
                ).build()
            }

            return INSTANCE;
        }

        fun cleanDbObject() {
            INSTANCE = null
        }
    }
}
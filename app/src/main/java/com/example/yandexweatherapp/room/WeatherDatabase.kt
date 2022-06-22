package com.example.yandexweatherapp.room

import android.content.Context
import androidx.room.*
import com.example.yandexweatherapp.models.OneApiCallWeatherDTO
import com.example.yandexweatherapp.room.entities.OneApiCallWeatherEntity
import com.example.yandexweatherapp.room.type_converters.CurrentTypeConverter
import com.example.yandexweatherapp.room.type_converters.DailyTypeConverter
import com.example.yandexweatherapp.room.type_converters.HourlyTypeConverter

@Database(entities = [OneApiCallWeatherDTO::class], version = 1)
@TypeConverters(
    HourlyTypeConverter::class,
    DailyTypeConverter::class, CurrentTypeConverter::class
)
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
                ).allowMainThreadQueries()
                    .build()
            }

            return INSTANCE;
        }

        fun cleanDbObject() {
            INSTANCE = null
        }
    }
}
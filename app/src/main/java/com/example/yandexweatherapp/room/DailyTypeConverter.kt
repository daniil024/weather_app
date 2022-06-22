package com.example.yandexweatherapp.room

import androidx.room.TypeConverter
import com.example.yandexweatherapp.models.DailyDTO
import com.example.yandexweatherapp.room.entities.DailyEntity
import com.example.yandexweatherapp.room.entities.HourlyEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DailyTypeConverter {

    companion object {
        private val gson: Gson = Gson()
    }

    @TypeConverter
    fun fromString(data: String?): List<DailyDTO?> {
        if (data == null) return Collections.emptyList();

        val listType = object : TypeToken<List<DailyDTO>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun toString(data : List<DailyDTO?>): String {
        return gson.toJson(data)
    }

}
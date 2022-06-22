package com.example.yandexweatherapp.room

import androidx.room.TypeConverter
import com.example.yandexweatherapp.models.HourlyDTO
import com.example.yandexweatherapp.room.entities.HourlyEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class HourlyTypeConverter {

    companion object {
        private val gson: Gson = Gson()
    }

    @TypeConverter
    fun fromString(data: String?): List<HourlyDTO?> {
        if (data == null) return Collections.emptyList();

        val listType = object : TypeToken<List<HourlyDTO>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun toString(data : List<HourlyDTO?>): String {
        return gson.toJson(data)
    }

}
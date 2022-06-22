package com.example.yandexweatherapp.room.type_converters

import androidx.room.TypeConverter
import com.example.yandexweatherapp.models.CurrentDTO
import com.google.gson.Gson

class CurrentTypeConverter {

    companion object {
        private val gson: Gson = Gson()
    }

    @TypeConverter
    fun fromString(data: String?): CurrentDTO? {
        if (data == null) return null;

        return gson.fromJson(data, CurrentDTO::class.java)
    }

    @TypeConverter
    fun toString(data : CurrentDTO?): String? {
        return gson.toJson(data)
    }

}
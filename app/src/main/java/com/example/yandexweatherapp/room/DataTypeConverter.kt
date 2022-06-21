package com.example.yandexweatherapp.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class DataTypeConverter {

    companion object {
        private val gson: Gson = Gson()
    }

    @TypeConverter
    fun fromString(data: String?): List<T?> {
        if (data == null) return Collections.emptyList();

        val listType = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun toString(data : List<T?>): String {
        return gson.toJson(data)
    }

}
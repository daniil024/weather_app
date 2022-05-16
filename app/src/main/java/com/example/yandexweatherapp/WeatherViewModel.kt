package com.example.yandexweatherapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.yandexweatherapp.utils.Constants

class WeatherViewModel(context: Application) : AndroidViewModel(context) {

//    fun setLocationPermissionGrant(isGranted: Boolean) {
//        val sharedPreferences =
//            getApplication<Application>().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(Constants.LOCATION_PERMISSION, isGranted)
//        editor.apply()
//    }
//
//    fun isLocationPermissionGranted(): Boolean {
//        val sharedPreferences =
//            getApplication<Application>().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
//        return sharedPreferences.getBoolean(Constants.LOCATION_PERMISSION, false)
//    }
}
package com.example.yandexweatherapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yandexweatherapp.models.OneApiCallWeatherDTO
import com.example.yandexweatherapp.network.OpenWeatherRetrofit
import com.example.yandexweatherapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WeatherViewModel(context: Application) : AndroidViewModel(context) {

    private val _coordinates = MutableLiveData<Pair<Double, Double>>()
    val coordinates: LiveData<Pair<Double, Double>>
        get() = _coordinates

    fun setCoordinates(lat:Double, lon:Double){
        _coordinates.value = Pair(lat, lon)
    }

    private val _oneCallApiCallWeatherDTO = MutableLiveData<OneApiCallWeatherDTO>()
    val oneCallApiCallWeatherDTO: LiveData<OneApiCallWeatherDTO>
        get() = _oneCallApiCallWeatherDTO

    suspend fun getWeather(lat: Double, lon: Double, lang: String) {
        _oneCallApiCallWeatherDTO.postValue(
            OpenWeatherRetrofit.openWeatherService.getOneCallWeather(
                lat,
                lon,
                lang
            )
        )
    }
}
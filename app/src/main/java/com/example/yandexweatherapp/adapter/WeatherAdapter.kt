package com.example.yandexweatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.yandexweatherapp.R
import com.example.yandexweatherapp.models.HourlyDailyWeather

class WeatherAdapter(context: Context, private val clickListener: OnWeatherRecyclerItemClicked) :
    RecyclerView.Adapter<HourlyViewHolder>() {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var imageLoader: RequestManager = Glide.with(context)

    init {
        val imageOption = RequestOptions().placeholder(R.drawable.image_placeholder)
            .fallback(R.drawable.image_placeholder).centerCrop()
        imageLoader.applyDefaultRequestOptions(imageOption)
    }

    private var weather: List<HourlyDailyWeather> = listOf()

    fun setWeather(hourly: List<HourlyDailyWeather>) {
        weather = hourly
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            layoutInflater.inflate(R.layout.small_weather_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.onBind(weather[position], imageLoader)
    }

    override fun getItemCount(): Int = weather.size

    fun replaceData(newItems: List<HourlyDailyWeather>) {

    }
}

interface OnWeatherRecyclerItemClicked {
    fun onClick(hourlyDailyWeather: HourlyDailyWeather)
}

package com.example.yandexweatherapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.yandexweatherapp.R
import com.example.yandexweatherapp.models.HourlyDTO
import com.example.yandexweatherapp.models.HourlyDailyWeather
import java.text.SimpleDateFormat
import java.util.*

sealed class HourlyDailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val imageIcon: ImageView = itemView.findViewById(R.id.smallCardIcon)
    protected val time: TextView = itemView.findViewById(R.id.smallCardTime)
    protected val temp: TextView = itemView.findViewById(R.id.smallCardTemp)
}

class DailyViewHolder(itemView: View) : HourlyDailyViewHolder(itemView) {

    fun onBind(hourly: HourlyDailyWeather, imageLoader: RequestManager) {
        hourly as HourlyDTO
        time.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(hourly.dt * 1000))
        temp.text = hourly.temp.toInt().toString()
        imageLoader.load("http://openweathermap.org/img/wn/${hourly.weather[0].icon}.png")
            .into(imageIcon)
    }
}

class HourlyViewHolder(itemView: View) : HourlyDailyViewHolder(itemView) {

    fun onBind(hourly: HourlyDailyWeather, imageLoader: RequestManager) {
        hourly as HourlyDTO
        time.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(hourly.dt * 1000))
        temp.text = hourly.temp.toInt().toString()
        imageLoader.load("http://openweathermap.org/img/wn/${hourly.weather[0].icon}.png")
            .into(imageIcon)
    }
}
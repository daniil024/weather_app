package com.example.yandexweatherapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.yandexweatherapp.R
import com.example.yandexweatherapp.models.DailyHourly
import com.example.yandexweatherapp.models.DailyHourlyType
import java.text.SimpleDateFormat
import java.util.*

class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageIcon: ImageView = itemView.findViewById(R.id.smallCardIcon)
    private val time: TextView = itemView.findViewById(R.id.smallCardTime)
    private val temp: TextView = itemView.findViewById(R.id.smallCardTemp)

    fun onBind(weather: DailyHourly, imageLoader: RequestManager) {
        if (weather.type == DailyHourlyType.DAILY) {
            time.textSize = 12f
            time.text = SimpleDateFormat(
                "MM-dd",
                Locale.ENGLISH
            ).format(Date(weather.dt * 1000)) + weather.dayTime
        } else {
            time.text = SimpleDateFormat(
                "yyyy-MM-dd hh:mm a",
                Locale.ENGLISH
            ).format(Date(weather.dt * 1000))
        }
        temp.text = weather.temp.toInt().toString()
        imageLoader.load("http://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
            .into(imageIcon)
    }
}
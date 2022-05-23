package com.example.yandexweatherapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.yandexweatherapp.R
import com.example.yandexweatherapp.models.HourlyDTO
import java.text.SimpleDateFormat
import java.util.*

class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageIcon: ImageView = itemView.findViewById(R.id.smallCardIcon)
    private val time: TextView = itemView.findViewById(R.id.smallCardTime)
    private val temp: TextView = itemView.findViewById(R.id.smallCardTemp)

    fun onBind(hourly: HourlyDTO, imageLoader: RequestManager) {
//        time.text = Date(hourly.dt).toString()
        time.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(hourly.dt * 1000))
        temp.text = hourly.temp.toInt().toString()
        imageLoader.load("http://openweathermap.org/img/wn/${hourly.weather[0].icon}.png").into(imageIcon)
    }
}
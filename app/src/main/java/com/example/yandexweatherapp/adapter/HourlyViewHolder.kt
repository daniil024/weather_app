package com.example.yandexweatherapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.yandexweatherapp.R
import com.example.yandexweatherapp.models.DailyDTO
import com.example.yandexweatherapp.models.DailyHourlyAdapter
import com.example.yandexweatherapp.models.HourlyDTO
import java.text.SimpleDateFormat
import java.util.*

sealed class BaseViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
    protected val imageIcon: ImageView = itemView.findViewById(R.id.smallCardIcon)
}

class DailyViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val tempMorn:TextView = itemView.findViewById(R.id.smallCardMornTemp)
    private val tempDay:TextView = itemView.findViewById(R.id.smallCardDayTemp)
    private val tempEve:TextView = itemView.findViewById(R.id.smallCardEveTemp)
    private val tempNight:TextView = itemView.findViewById(R.id.smallCardNightTemp)

    fun onBind(weather: DailyHourlyAdapter, imageLoader: RequestManager) {
        weather as DailyDTO
        tempMorn.text = weather.temp.morn.toInt().toString()+"%"
        tempDay.text = weather.temp.day.toInt().toString()+"%"
        tempEve.text = weather.temp.eve.toInt().toString()+"%"
        tempNight.text = weather.temp.night.toInt().toString()+"%"
        imageLoader.load("http://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
            .into(imageIcon)
    }
}

class HourlyViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val time: TextView = itemView.findViewById(R.id.smallCardTime)
    private val temp: TextView = itemView.findViewById(R.id.smallCardTemp)

    fun onBind(weather: DailyHourlyAdapter, imageLoader: RequestManager) {
        weather as HourlyDTO
        time.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weather.dt * 1000))
        temp.text = weather.temp.toInt().toString()
        imageLoader.load("http://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
            .into(imageIcon)
    }
}
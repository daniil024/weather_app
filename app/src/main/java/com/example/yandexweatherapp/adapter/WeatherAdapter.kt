package com.example.yandexweatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.yandexweatherapp.R
import com.example.yandexweatherapp.models.DailyHourly
import com.example.yandexweatherapp.models.DailyHourlyAdapter
import com.example.yandexweatherapp.models.HourlyDTO

class WeatherAdapter(context: Context, private val clickListener: OnWeatherRecyclerItemClicked) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private val VIEW_TYPE_ONE = 1
    private val VIEW_TYPE_TWO = 2

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var imageLoader: RequestManager = Glide.with(context)

    init {
        val imageOption = RequestOptions().placeholder(R.drawable.image_placeholder)
            .fallback(R.drawable.image_placeholder).centerCrop()
        imageLoader.applyDefaultRequestOptions(imageOption)
    }

    private var weather: List<DailyHourlyAdapter> = listOf()

    fun setWeather(weather: List<DailyHourlyAdapter>) {
        this.weather = weather
    }

    override fun getItemViewType(position: Int): Int {
        return if (weather[position] is HourlyDTO) {
            VIEW_TYPE_ONE
        } else {
            VIEW_TYPE_TWO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            HourlyViewHolder(
                layoutInflater.inflate(R.layout.small_weather_card, parent, false)
            )
        } else {
            DailyViewHolder(
                layoutInflater.inflate(R.layout.small_weather_card_daily, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (weather[position] is HourlyDTO) {
            holder as HourlyViewHolder
            holder.onBind(weather[position], imageLoader)
            holder.itemView.setOnClickListener { clickListener.onClick(weather[position]) }
        } else {
            holder as DailyViewHolder
            holder.onBind(weather[position], imageLoader)
            holder.itemView.setOnClickListener { clickListener.onClick(weather[position]) }
        }
    }

    override fun getItemCount(): Int = weather.size

    fun replaceData(newItems: List<DailyHourlyAdapter>) {
        val weatherDiffUtilsCallback = WeatherDiffUtilsCallback(weather, newItems)
        val diffResult = DiffUtil.calculateDiff(weatherDiffUtilsCallback)
        this.setWeather(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

interface OnWeatherRecyclerItemClicked {
    fun onClick(hourlyDailyWeather: DailyHourlyAdapter)
}

package com.example.yandexweatherapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.yandexweatherapp.models.DailyHourlyAdapter

class WeatherDiffUtilsCallback(
    private val oldList: List<DailyHourlyAdapter>,
    private val newList: List<DailyHourlyAdapter>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]===newList[newItemPosition]
    }
}
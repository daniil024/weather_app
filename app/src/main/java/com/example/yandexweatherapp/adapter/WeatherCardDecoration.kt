package com.example.yandexweatherapp.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class WeatherCardDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        when (parent.getChildLayoutPosition(view)) {
            0 -> {
                outRect.set(0, offset, offset, offset)
            }
            state.itemCount - 1 -> {
                outRect.set(offset, offset, 0, offset)
            }
            else -> {
                outRect.set(offset, offset, offset, offset)
            }
        }
    }
}
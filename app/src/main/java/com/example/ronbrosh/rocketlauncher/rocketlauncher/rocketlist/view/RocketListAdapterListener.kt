package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ronbrosh.rocketlauncher.model.Rocket

interface RocketListAdapterListener {
    fun onRocketItemClick(viewHolder: RecyclerView.ViewHolder, rocket: Rocket)

    fun onLastSelectedItemLoadFinished()
}
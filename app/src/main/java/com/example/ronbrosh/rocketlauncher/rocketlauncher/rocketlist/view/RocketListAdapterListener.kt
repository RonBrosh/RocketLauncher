package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.view.View
import com.example.ronbrosh.rocketlauncher.model.Rocket

interface RocketListAdapterListener {
    fun onRocketItemClick(view: View, rocket: Rocket)

    fun onLastSelectedItemLoadFinished()
}
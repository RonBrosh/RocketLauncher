package com.example.ronbrosh.rocketlauncher.rocketlist.view

import android.view.View
import com.example.ronbrosh.rocketlauncher.model.Rocket

interface RocketListItemClickListener {
    fun onRocketItemClick(view: View, rocket: Rocket)
}
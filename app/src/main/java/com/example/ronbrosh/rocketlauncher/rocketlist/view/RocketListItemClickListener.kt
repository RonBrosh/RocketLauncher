package com.example.ronbrosh.rocketlauncher.rocketlist.view

import com.example.ronbrosh.rocketlauncher.model.Rocket

interface RocketListItemClickListener {
    fun onRocketItemClick(rocket: Rocket)
}
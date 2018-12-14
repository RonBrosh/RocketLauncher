package com.example.ronbrosh.rocketlauncher.rocketlist.view

import com.example.ronbrosh.rocketlauncher.db.RocketData

interface RocketListItemClickListener {
    fun onRocketItemClick(rocketData: RocketData)
}
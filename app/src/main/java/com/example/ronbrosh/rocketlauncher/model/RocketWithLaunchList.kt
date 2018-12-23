package com.example.ronbrosh.rocketlauncher.model

import androidx.room.Embedded
import androidx.room.Relation

data class RocketWithLaunchList(
        @Embedded var rocket: Rocket,
        @Relation(parentColumn = "rocketId", entityColumn = "rocketId") var launchList: List<Launch>
)
package com.example.ronbrosh.rocketlauncher.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Engine(
        @SerializedName("number") @ColumnInfo(name = "enginesCount") var enginesCount: Int
)
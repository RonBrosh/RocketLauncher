package com.example.ronbrosh.rocketlauncher.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

open class Rocket(
        @SerializedName("rocket_name") @ColumnInfo(name = "name") var name: String,
        @SerializedName("country") @ColumnInfo(name = "country") var country: String,
        @Embedded @SerializedName("engines") var engine: Engine
)
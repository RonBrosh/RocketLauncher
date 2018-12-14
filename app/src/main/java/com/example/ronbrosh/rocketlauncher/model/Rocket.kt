package com.example.ronbrosh.rocketlauncher.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rocketTable")
data class Rocket(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @SerializedName("rocket_name") @ColumnInfo(name = "name") var name: String,
        @SerializedName("country") @ColumnInfo(name = "country") var country: String,
        @SerializedName("active") @ColumnInfo(name = "isActive") var isActive: Boolean,
        @Embedded @SerializedName("engines") var engine: Engine
)
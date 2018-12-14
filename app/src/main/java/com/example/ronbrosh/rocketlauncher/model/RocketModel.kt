package com.example.ronbrosh.rocketlauncher.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rocketTable")
data class RocketModel(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @SerializedName("rocket_name") @ColumnInfo(name = "name") var name: String,
        @SerializedName("country") @ColumnInfo(name = "country") var country: String,
        @Embedded @SerializedName("engines") var engineModel: EngineModel
)

data class EngineModel(
        @SerializedName("number") @ColumnInfo(name = "enginesCount") var enginesCount: Int
)
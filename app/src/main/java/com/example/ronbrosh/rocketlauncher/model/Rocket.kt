package com.example.ronbrosh.rocketlauncher.model

import androidx.room.*
import com.example.ronbrosh.rocketlauncher.db.RocketConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rocketTable")
data class Rocket(
        @SerializedName("id") @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) var id: Long,
        @SerializedName("rocket_name") @ColumnInfo(name = "name") var name: String,
        @SerializedName("country") @ColumnInfo(name = "country") var country: String,
        @SerializedName("active") @ColumnInfo(name = "isActive") var isActive: Boolean,
        @Embedded @SerializedName("engines") var engine: Engine,
        @SerializedName("flickr_images") @TypeConverters(RocketConverters::class) @ColumnInfo(name = "imageUrlList") var imageUrlList: List<String>
)
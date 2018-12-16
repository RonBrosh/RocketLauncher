package com.example.ronbrosh.rocketlauncher.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Launch(
        @ColumnInfo(name = "rocketId") @PrimaryKey(autoGenerate = false) var rocketId: Long,
        @SerializedName("flight_number") @ColumnInfo(name = "flightNumber") @PrimaryKey(autoGenerate = false) var flightNumber: Long,
        @SerializedName("launch_year") @ColumnInfo(name = "year") var year: String,
        @SerializedName("mission_name") @ColumnInfo(name = "name") var name: String,
        @SerializedName("launch_date_unix") @ColumnInfo(name = "timeStamp") var timeStamp: Long,
        @SerializedName("launch_success") @ColumnInfo(name = "isSuccessful") var isSuccessful: Boolean,
        @Embedded @SerializedName("links") var patchImage: PatchImage
)

data class PatchImage(
        @SerializedName("mission_patch") @ColumnInfo(name = "bigImageURL") var bigImageURL: String,
        @SerializedName("mission_patch_small") @ColumnInfo(name = "smallImageURL") var smallImageURL: String
)
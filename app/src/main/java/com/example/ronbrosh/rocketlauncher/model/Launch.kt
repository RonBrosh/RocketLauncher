package com.example.ronbrosh.rocketlauncher.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "launchTable", indices = [Index(value = ["rocketId", "flightNumber"], unique = true)])
data class Launch(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo(name = "rocketId") var rocketId: Long,
        @SerializedName("flight_number") @ColumnInfo(name = "flightNumber") var flightNumber: Long,
        @SerializedName("launch_year") @ColumnInfo(name = "year") var year: String,
        @SerializedName("mission_name") @ColumnInfo(name = "name") var name: String,
        @SerializedName("launch_date_unix") @ColumnInfo(name = "timeStamp") var timeStamp: Long,
        @SerializedName("launch_success") @ColumnInfo(name = "isSuccessful") var isSuccessful: Boolean,
        @Embedded @SerializedName("links") var patchImage: PatchImage
)
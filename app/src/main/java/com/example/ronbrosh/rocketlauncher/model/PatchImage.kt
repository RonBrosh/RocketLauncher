package com.example.ronbrosh.rocketlauncher.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class PatchImage(
        @SerializedName("mission_patch") @ColumnInfo(name = "bigImageURL") var bigImageURL: String?,
        @SerializedName("mission_patch_small") @ColumnInfo(name = "smallImageURL") var smallImageURL: String?
)
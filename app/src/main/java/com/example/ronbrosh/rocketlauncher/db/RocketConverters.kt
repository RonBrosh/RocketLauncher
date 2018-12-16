package com.example.ronbrosh.rocketlauncher.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RocketConverters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun stringToStringList(string: String?): List<String>? {
            string?.let {
                return Gson().fromJson(it, object : TypeToken<List<String>>() {}.type)
            }
            return null
        }

        @TypeConverter
        @JvmStatic
        fun stringListToString(stringList: List<String>?): String? {
            stringList?.let {
                return Gson().toJson(stringList, object : TypeToken<List<String>>() {}.type)
            }
            return null
        }
    }
}
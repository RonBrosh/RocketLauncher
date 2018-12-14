package com.example.ronbrosh.rocketlauncher.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RocketData::class], version = 1)
abstract class RocketDataBase : RoomDatabase() {

    abstract fun getRocketDataDao(): RocketDataDao

    companion object {
        private var instance: RocketDataBase? = null

        @Synchronized
        fun getInstance(context: Context): RocketDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        RocketDataBase::class.java, "rocket.db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance as RocketDataBase
        }
    }
}
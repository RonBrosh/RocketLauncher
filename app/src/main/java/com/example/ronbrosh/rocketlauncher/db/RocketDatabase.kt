package com.example.ronbrosh.rocketlauncher.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ronbrosh.rocketlauncher.model.Rocket

@Database(entities = [Rocket::class], version = 1)
@TypeConverters(RocketConverters::class)
abstract class RocketDatabase : RoomDatabase() {
    abstract fun getRocketDao(): RocketDao

    companion object {
        private var instance: RocketDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RocketDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        RocketDatabase::class.java, "rocket.db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance as RocketDatabase
        }
    }
}
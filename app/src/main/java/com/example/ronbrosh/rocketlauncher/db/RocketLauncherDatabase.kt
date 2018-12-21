package com.example.ronbrosh.rocketlauncher.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ronbrosh.rocketlauncher.db.dao.LaunchDao
import com.example.ronbrosh.rocketlauncher.db.dao.RocketDao
import com.example.ronbrosh.rocketlauncher.db.dao.RocketWithLaunchListDao
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.Rocket

@Database(entities = [Rocket::class, Launch::class], version = 1)
@TypeConverters(RocketConverters::class)
abstract class RocketLauncherDatabase : RoomDatabase() {
    abstract fun getRocketDao(): RocketDao
    abstract fun getLaunchDao(): LaunchDao
    abstract fun getRocketWithLaunchListDao(): RocketWithLaunchListDao

    companion object {
        private var instance: RocketLauncherDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RocketLauncherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        RocketLauncherDatabase::class.java, "rocketLauncher.db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance as RocketLauncherDatabase
        }
    }
}
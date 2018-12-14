package com.example.ronbrosh.rocketlauncher.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RocketDataDao {
    @Query("SELECT * from rocketData")
    fun getAllRocketData(): LiveData<List<RocketData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRocketData(rocketData: RocketData)

    @Query("DELETE from rocketData")
    fun deleteAllRocketData()
}
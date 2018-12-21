package com.example.ronbrosh.rocketlauncher.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ronbrosh.rocketlauncher.model.Rocket

@Dao
interface RocketDao {
    @Query("SELECT * from rocketTable")
    fun getRocketListLiveData(): LiveData<List<Rocket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRocket(rocket: Rocket)

    @Query("DELETE from rocketTable")
    fun deleteRocketTable()
}
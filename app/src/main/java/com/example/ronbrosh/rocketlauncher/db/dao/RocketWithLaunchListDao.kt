package com.example.ronbrosh.rocketlauncher.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.ronbrosh.rocketlauncher.model.RocketWithLaunchList

@Dao
interface RocketWithLaunchListDao {
    @Query("SELECT * from rocketTable WHERE rocketId == :rocketId")
    fun getRocketWithLaunchListLiveData(rocketId: String): LiveData<RocketWithLaunchList>
}
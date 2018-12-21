package com.example.ronbrosh.rocketlauncher.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ronbrosh.rocketlauncher.model.Launch

@Dao
interface LaunchDao {
    @Query("SELECT * from launchTable")
    fun getLaunchListLiveData(): LiveData<List<Launch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaunch(launch: Launch)

    @Query("DELETE from launchTable")
    fun deleteLaunchTable()
}
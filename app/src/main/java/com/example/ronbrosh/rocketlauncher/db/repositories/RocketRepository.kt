package com.example.ronbrosh.rocketlauncher.db.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.ronbrosh.rocketlauncher.db.RocketLauncherDatabase
import com.example.ronbrosh.rocketlauncher.db.dao.RocketDao
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.utils.AppUtil

class RocketRepository(application: Application) {
    private val rocketDao: RocketDao
    private val rocketListLiveData: LiveData<List<Rocket>>

    init {
        val rocketLauncherDatabase: RocketLauncherDatabase = RocketLauncherDatabase.getInstance(application)
        rocketDao = rocketLauncherDatabase.getRocketDao()
        rocketListLiveData = rocketDao.getRocketListLiveData()
    }

    fun insertRocket(rocket: Rocket) {
        AppUtil.runOnBackgroundThread {
            rocketDao.insertRocket(rocket)
        }
    }

    fun getRocketListLiveData(): LiveData<List<Rocket>> {
        return rocketListLiveData
    }

    fun deleteRocketTable() {
        AppUtil.runOnBackgroundThread {
            rocketDao.deleteRocketTable()
        }
    }
}
package com.example.ronbrosh.rocketlauncher.db

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.ronbrosh.rocketlauncher.utils.AppUtil

class RocketDataRepository(application: Application) {
    private val rocketDataDao: RocketDataDao
    private val allRocketData: LiveData<List<RocketData>>

    init {
        val rocketDataBase: RocketDataBase = RocketDataBase.getInstance(application)
        rocketDataDao = rocketDataBase.getRocketDataDao()
        allRocketData = rocketDataDao.getAllRocketData()
    }

    fun insertRocketData(rocketData: RocketData) {
        AppUtil.runOnBackgroundThread {
            rocketDataDao.insertRocketData(rocketData)
        }
    }

    fun getAllRocketData(): LiveData<List<RocketData>> {
        return allRocketData
    }

    fun deleteAllRocketData() {
        AppUtil.runOnBackgroundThread {
            rocketDataDao.deleteAllRocketData()
        }
    }
}
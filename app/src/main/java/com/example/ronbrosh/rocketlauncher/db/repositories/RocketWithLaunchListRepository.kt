package com.example.ronbrosh.rocketlauncher.db.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.ronbrosh.rocketlauncher.db.RocketLauncherDatabase
import com.example.ronbrosh.rocketlauncher.db.dao.LaunchDao
import com.example.ronbrosh.rocketlauncher.db.dao.RocketWithLaunchListDao
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.RocketWithLaunchList
import com.example.ronbrosh.rocketlauncher.utils.AppUtil

class RocketWithLaunchListRepository(application: Application, rocketId: String) {
    private val rocketWithLaunchListDao: RocketWithLaunchListDao
    private val launchDao: LaunchDao
    private val rocketWithLaunchListLiveData: LiveData<RocketWithLaunchList>

    init {
        val rocketLauncherDatabase: RocketLauncherDatabase = RocketLauncherDatabase.getInstance(application)
        rocketWithLaunchListDao = rocketLauncherDatabase.getRocketWithLaunchListDao()
        launchDao = rocketLauncherDatabase.getLaunchDao()
        rocketWithLaunchListLiveData = rocketWithLaunchListDao.getRocketWithLaunchListLiveData(rocketId)
    }

    fun getRocketWithLaunchListLiveData(): LiveData<RocketWithLaunchList> {
        return rocketWithLaunchListLiveData
    }

    fun insertLaunch(launch: Launch) {
        AppUtil.runOnBackgroundThread {
            launchDao.insertLaunch(launch)
        }
    }
}
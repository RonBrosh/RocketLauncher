package com.example.ronbrosh.rocketlauncher.db.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.ronbrosh.rocketlauncher.db.RocketLauncherDatabase
import com.example.ronbrosh.rocketlauncher.db.dao.LaunchDao
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.utils.AppUtil

class LaunchRepository(application: Application) {
    private val launchDao: LaunchDao
    private val launchListLiveData: LiveData<List<Launch>>

    init {
        val rocketLauncherDatabase: RocketLauncherDatabase = RocketLauncherDatabase.getInstance(application)
        launchDao = rocketLauncherDatabase.getLaunchDao()
        launchListLiveData = launchDao.getLaunchListLiveData()
    }

    fun insertLaunch(launch: Launch) {
        AppUtil.runOnBackgroundThread {
            launchDao.insertLaunch(launch)
        }
    }

    fun getLaunchListLiveData(): LiveData<List<Launch>> {
        return launchListLiveData
    }

    fun deleteLaunchTable() {
        AppUtil.runOnBackgroundThread {
            launchDao.deleteLaunchTable()
        }
    }
}
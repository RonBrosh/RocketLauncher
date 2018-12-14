package com.example.ronbrosh.rocketlauncher.rocketlist.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ronbrosh.rocketlauncher.api.RocketApi
import com.example.ronbrosh.rocketlauncher.db.RocketData
import com.example.ronbrosh.rocketlauncher.db.RocketDataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RocketListViewModel(application: Application) : ViewModel() {
    private val rocketDataRepository: RocketDataRepository = RocketDataRepository(application)
    private val allRocketData: LiveData<List<RocketData>>
    private val rocketApi: RocketApi = RocketApi.Factory().create()

    init {
        allRocketData = rocketDataRepository.getAllRocketData()
    }

    fun insertRocketData(rocketData: RocketData) {
        rocketDataRepository.insertRocketData(rocketData)
    }

    fun getAllRocketData(): LiveData<List<RocketData>> {
        return allRocketData
    }

    fun deleteAllRocketData() {
        rocketDataRepository.deleteAllRocketData()
    }

    fun fetchRocketDataFromServer() {
        allRocketData.value?.let {
            rocketApi.getAllRockets().enqueue(object : Callback<List<RocketData>> {
                override fun onFailure(call: Call<List<RocketData>>, t: Throwable) {
                }

                override fun onResponse(call: Call<List<RocketData>>, response: Response<List<RocketData>>) {
                    response.body()?.let { result ->
                        result.forEach { nextRocketData ->
                            rocketDataRepository.insertRocketData(nextRocketData)
                        }
                    }
                }
            })
        }
    }
}
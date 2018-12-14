package com.example.ronbrosh.rocketlauncher.rocketlist.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ronbrosh.rocketlauncher.api.RocketApi
import com.example.ronbrosh.rocketlauncher.db.RocketRepository
import com.example.ronbrosh.rocketlauncher.model.Rocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RocketListViewModel(application: Application) : ViewModel() {
    private val rocketRepository: RocketRepository = RocketRepository(application)
    private val rocketListLiveData: LiveData<List<Rocket>>
    private val rocketApi: RocketApi = RocketApi.Factory().create()

    init {
        rocketListLiveData = rocketRepository.getRocketListLiveData()
    }

    fun insertRocket(rocket: Rocket) {
        rocketRepository.insertRocket(rocket)
    }

    fun getRocketListLiveData(): LiveData<List<Rocket>> {
        return rocketListLiveData
    }

    fun deleteRocketTable() {
        rocketRepository.deleteRocketTable()
    }

    fun fetchRocketList() {
        rocketListLiveData.value?.let {
            rocketApi.fetchRocketList().enqueue(object : Callback<List<Rocket>> {
                override fun onFailure(call: Call<List<Rocket>>, t: Throwable) {
                }

                override fun onResponse(call: Call<List<Rocket>>, response: Response<List<Rocket>>) {
                    response.body()?.let { result ->
                        result.forEach { nextRocket ->
                            rocketRepository.insertRocket(nextRocket)
                        }
                    }
                }
            })
        }
    }
}
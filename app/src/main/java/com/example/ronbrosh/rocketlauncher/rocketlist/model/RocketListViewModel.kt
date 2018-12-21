package com.example.ronbrosh.rocketlauncher.rocketlist.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ronbrosh.rocketlauncher.api.RocketApi
import com.example.ronbrosh.rocketlauncher.db.repositories.RocketRepository
import com.example.ronbrosh.rocketlauncher.model.Rocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RocketListViewModel(application: Application) : ViewModel() {
    private val rocketRepository: RocketRepository = RocketRepository(application)
    private val rocketListLiveData: LiveData<List<Rocket>>
    private val loadingLiveData: MutableLiveData<Boolean>

    init {
        rocketListLiveData = rocketRepository.getRocketListLiveData()
        loadingLiveData = MutableLiveData()
    }

    fun insertRocket(rocket: Rocket) {
        rocketRepository.insertRocket(rocket)
    }

    fun getRocketListLiveData(): LiveData<List<Rocket>> {
        return rocketListLiveData
    }

    fun getLoadingLiveData(): MutableLiveData<Boolean> {
        return loadingLiveData
    }

    fun deleteRocketTable() {
        rocketRepository.deleteRocketTable()
    }

    fun fetchRocketList() {
        if (loadingLiveData.value == true)
            return

        loadingLiveData.value = true
        RocketApi.Factory.getInstance().fetchRocketList().enqueue(object : Callback<List<Rocket>> {
            override fun onFailure(call: Call<List<Rocket>>, t: Throwable) {
                loadingLiveData.value = false
            }

            override fun onResponse(call: Call<List<Rocket>>, response: Response<List<Rocket>>) {
                response.body()?.let { result ->
                    result.forEach { nextRocket ->
                        rocketRepository.insertRocket(nextRocket)
                    }
                }
                loadingLiveData.value = false
            }
        })
    }

    fun getFilteredRocketList(isFilter: Boolean): List<Rocket>? {
        rocketListLiveData.value?.let {
            return if (isFilter) it.filter { rocket -> rocket.isActive } else it
        }

        return null
    }
}
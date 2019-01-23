package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ronbrosh.rocketlauncher.api.RocketApi
import com.example.ronbrosh.rocketlauncher.db.repositories.RocketWithLaunchListRepository
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.RocketWithLaunchList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RocketDetailsViewModel(application: Application, rocketId: String) : ViewModel() {
    private val rocketWithLaunchListRepository: RocketWithLaunchListRepository = RocketWithLaunchListRepository(application, rocketId)
    private val rocketWithLaunchListLiveData: LiveData<RocketWithLaunchList>
    private val loadingLiveData: MutableLiveData<Boolean>

    init {
        rocketWithLaunchListLiveData = rocketWithLaunchListRepository.getRocketWithLaunchListLiveData()
        loadingLiveData = MutableLiveData()
    }

    fun getRocketWithLaunchListLiveData(): LiveData<RocketWithLaunchList> {
        return rocketWithLaunchListLiveData
    }

    fun getLoadingLiveData(): MutableLiveData<Boolean> {
        return loadingLiveData
    }

    fun fetchRocketLaunchList(rocketId: String) {
        loadingLiveData.value = true
        RocketApi.Factory.getInstance().fetchRocketLaunchesList(rocketId).enqueue(object : Callback<List<Launch>> {
            override fun onFailure(call: Call<List<Launch>>, t: Throwable) {
                loadingLiveData.value = false
            }

            override fun onResponse(call: Call<List<Launch>>, response: Response<List<Launch>>) {
                response.body()?.let { result ->
                    rocketWithLaunchListLiveData.value?.let {
                        result.forEach { nextLaunch ->
                            nextLaunch.rocketId = it.rocket.rocketId
                            rocketWithLaunchListRepository.insertLaunch(nextLaunch)
                        }
                    }
                }
                loadingLiveData.value = false
            }
        })
    }
}
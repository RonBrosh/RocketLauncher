package com.example.ronbrosh.rocketlauncher.api

import com.example.ronbrosh.rocketlauncher.db.RocketData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RocketApi {
    @GET("v3/rockets")
    fun getAllRockets(): Call<List<RocketData>>

    class Factory {
        fun create(): RocketApi {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.spacexdata.com/")
                    .build()

            return retrofit.create(RocketApi::class.java)
        }
    }
}
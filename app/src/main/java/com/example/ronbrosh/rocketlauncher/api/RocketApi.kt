package com.example.ronbrosh.rocketlauncher.api

import com.example.ronbrosh.rocketlauncher.model.Rocket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RocketApi {
    @GET("v3/rockets")
    fun fetchRocketList(): Call<List<Rocket>>

    @GET("v3/launches")
    fun fetchRocketLaunchesList(@Query("\"rocket_id\"") rocketId: Long): Call<Void>

    class Factory {
        companion object {
            private var instance: RocketApi? = null

            fun getInstance(): RocketApi {
                if (instance == null) {
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                    val retrofit = Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl("https://api.spacexdata.com/")
                            .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
                            .build()

                    instance = retrofit.create(RocketApi::class.java)
                }

                return instance!!
            }
        }
    }
}
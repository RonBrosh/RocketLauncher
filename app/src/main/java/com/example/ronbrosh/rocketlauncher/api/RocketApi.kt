package com.example.ronbrosh.rocketlauncher.api

import com.example.ronbrosh.rocketlauncher.model.Rocket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RocketApi {
    @GET("v3/rockets")
    fun fetchRocketList(): Call<List<Rocket>>

    class Factory {
        fun create(): RocketApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.spacexdata.com/")
                    .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
                    .build()

            return retrofit.create(RocketApi::class.java)
        }
    }
}
//
//HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//Retrofit retrofit = new Retrofit.Builder()
//.baseUrl("https://backend.example.com")
//.client(client)
//.addConverterFactory(GsonConverterFactory.create())
//.build();
//
//return retrofit.create(ApiClient.class);

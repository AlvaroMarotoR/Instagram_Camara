package com.example.insta.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MiRetrofitBuilder{

    private const val IP = "192.168.1.44"
    private const val PUERTO = "8000"
    private const val BASE_URL = "http://$IP:$PUERTO"

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService:ApiService by lazy{
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

}
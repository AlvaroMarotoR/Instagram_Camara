package com.example.insta.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MiRetrofitBuilder{

    const val IP = "192.168.1.42"
    const val PUERTO = "8000"
    const val BASE_URL = "http://192.168.1.42:8000"

    val retrofitBuilder: Retrofit.Builder by lazy {
    Log.d("DEBUG", "ENE L APICONSTRUCTOR")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService:ApiService by lazy{
        Log.d("DEBUG", "ENE L APISDAERVIO")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

}
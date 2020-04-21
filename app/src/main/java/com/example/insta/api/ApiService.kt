package com.example.insta.api

import com.example.insta.models.LoginUser
import com.example.insta.models.Token
import com.example.insta.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/usuarios/login")
    fun loginUser(@Body loginUser: LoginUser): Call<Token>

    @GET("api/usuarios/get")
    fun getUserLogged(
        @Header("Authorization: Token ") token: String
    ):Call<User>
}
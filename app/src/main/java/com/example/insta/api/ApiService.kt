package com.example.insta.api

import com.example.insta.models.LoginUser
import com.example.insta.models.Token
import com.example.insta.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/usuarios/login")
    fun loginUser(@Body loginUser: LoginUser): Call<Token>

    @GET("api/usuarios/get")
    fun getUserLogged(
        @Header("Authorization") token: String
    ):Call<User>

    @Multipart
    @POST("api/usuarios/register")
    fun register(
        @Part("email") email:RequestBody,
        @Part("username") username:RequestBody,
        @Part("nombre") nombre:RequestBody,
        @Part("apellido1") apellido1:RequestBody,
        @Part("apellido2") apellido2:RequestBody,
        @Part("fecha_nacimiento") fecha_nacimiento:RequestBody,
        @Part("telefono") telefono:RequestBody,
        @Part("password") password1:RequestBody,
        @Part("password2") password2:RequestBody,
        @Part("genero") genero:RequestBody,
        @Part imagen:MultipartBody.Part?
    ):Call<ResponseBody>

}
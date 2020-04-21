package com.example.insta.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginUser(
    @Expose
    @SerializedName("username")
    val email: String,

    @Expose
    @SerializedName("password")
    val password: String
){
    override fun toString(): String {
        return "Login: username: $email, password: $password"
    }
}
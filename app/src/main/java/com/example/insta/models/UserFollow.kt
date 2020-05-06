package com.example.insta.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserFollow(
    @Expose
    @SerializedName("usuario_seguidor")
    val usuario_seguidor: String,

    @Expose
    @SerializedName("usuario_seguido")
    val usuario_seguido: String,

    @Expose
    @SerializedName("fecha_seguido")
    val fecha_seguido: String
){

}
package com.example.insta.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("pk")
    val pk: String,

    @Expose
    @SerializedName("email")
    val email: String,

    @Expose
    @SerializedName("username")
    val username: String,

    @Expose
    @SerializedName("nombre")
    val nombre: String,

    @Expose
    @SerializedName("apellido1")
    val apellido1: String,

    @Expose
    @SerializedName("apellido2")
    val apellido2: String,

    @Expose
    @SerializedName("fecha_nacimiento")
    val fecha_nacimiento: String,

    @Expose
    @SerializedName("telefono")
    val telefono: String,

    @Expose
    @SerializedName("descripcion")
    val descripcion: String,

    @Expose
    @SerializedName("imagen")
    val imagen: String,

    @Expose
    @SerializedName("genero")
    val genero: String,

    @Expose
    @SerializedName("n_seguidores")
    val n_seguidores: Int,

    @Expose
    @SerializedName("n_seguidos")
    val n_seguidos: Int,

    @Expose
    @SerializedName("seguidos")
    val seguidos: ArrayList<UserFollow>,

    @Expose
    @SerializedName("seguidores")
    val seguidores: ArrayList<UserFollow>

) {
}
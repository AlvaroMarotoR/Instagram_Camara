package com.example.insta.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comentario(
    @Expose
    @SerializedName("usuario")
    val usuario: String,

    @Expose
    @SerializedName("publicacion")
    val publicacion: String,

    @Expose
    @SerializedName("comentario")
    val comentario: String,

    @Expose
    @SerializedName("fecha_publicado")
    val fecha_publicado: String
){

}
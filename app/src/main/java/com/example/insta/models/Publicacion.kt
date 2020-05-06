package com.example.insta.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Publicacion (

    @Expose
    @SerializedName("usuario")
    val usuario: String,

    @Expose
    @SerializedName("descripcion")
    val descripcion: String,

    @Expose
    @SerializedName("fecha_publicado")
    val fecha_publicado: String,

    @Expose
    @SerializedName("localizacion")
    val localizacion: String,

    @Expose
    @SerializedName("n_likes")
    val n_likes: Int,

    @Expose
    @SerializedName("n_comentarios")
    val n_comentarios: Int,

    @Expose
    @SerializedName("es_imagen")
    val es_imagen: Boolean,

    @Expose
    @SerializedName("imagen")
    val imagenUrl: String?,

    @Expose
    @SerializedName("video")
    val videoUrl: String?,

    @Expose
    @SerializedName("comentarios")
    val comentarios: ArrayList<Comentario>,

    @Expose
    @SerializedName("megustas")
    val megustas: ArrayList<MeGusta>



){

}
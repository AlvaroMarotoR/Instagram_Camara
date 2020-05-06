package com.example.insta.recyclerview

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.insta.R
import com.example.insta.utils.MiViewUtils
import kotlinx.android.synthetic.main.activity_logged.*
import kotlinx.android.synthetic.main.publicacion_listitem.view.*

class PublicacionAdapter(private val publicacionList: ArrayList<PublicacionItem>) : RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>(){

    class PublicacionViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val userImage = itemView.userImagePublicacion
        val txtUsernameHeader = itemView.txtUsernameHeaderPublicacion
        val txtLocalizacion = itemView.txtLocalizacionPublicacion
        val btnAction = itemView.btnActionPublicacion
        val imagenPublicacion = itemView.imgPublicacion
        val videoPublicacion= itemView.videoPublicacion
        val btnLike = itemView.btnLikePublicacion
        val btnComment = itemView.btnCommentPublicacion
        val btnSave = itemView.btnSavePublicacion
        val txtLike = itemView.txtLikesPublicacion
        val txtUsernameBody = itemView.txtUsernameBodyPublicacion
        val txtDescripcion = itemView.txtDescripcionPublicacion
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.publicacion_listitem, parent, false)
        return PublicacionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return publicacionList.size
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val publicacion = publicacionList[position].publicacion

        holder.userImage.setImageResource(R.drawable.destello)
        holder.txtUsernameHeader.text = publicacion.usuario
        holder.txtLocalizacion.text = publicacion.localizacion
        //holder.btnAction = itemView.btnActionPublicacion
        if(publicacion.es_imagen){
            //Glide.with(holder.itemView).load("http://192.168.1.44:8000/media/usuarios/None-uwuuushsh.jpg").into(holder.imagenPublicacion)
            MiViewUtils.showView(holder.imagenPublicacion)

        } else{
            //holder.videoPublicacion= itemView.videoPublicacion

        }
        //holder.btnLike = itemView.btnLikePublicacion
        //holder.btnComment = itemView.btnCommentPublicacion
        //holder.btnSave = itemView.btnSavePublicacion
        holder.txtLike.text = publicacion.n_likes.toString()
        holder.txtUsernameBody.text = publicacion.usuario
        holder.txtDescripcion.text = publicacion.descripcion


    }
}
package com.example.insta.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.insta.R
import com.example.insta.models.Comentario
import com.example.insta.models.MeGusta
import com.example.insta.models.Publicacion
import com.example.insta.recyclerview.PublicacionAdapter
import com.example.insta.recyclerview.PublicacionItem
import com.example.insta.utils.MiViewUtils
import com.example.insta.views.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val publicacionList = ArrayList<PublicacionItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAppBarHome.setNavigationOnClickListener {
            MiViewUtils.showToast("diste al tate", requireContext())
        }
        topAppBarHome.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.appBarHomeMensaje->{
                    MiViewUtils.showToast("ditehome", requireContext())
                    true
                }
                else->
                    false

            }
        }
        publicacionList.add(
            PublicacionItem(Publicacion("Pepe", "madre flipa que pedazo de descripcion me estoy marcando con el tate"
                ,"2000-01-01", "Madrid", 10, 1, true, null, null, ArrayList<Comentario>(), ArrayList<MeGusta>()))
        )
        publicacionList.add(
            PublicacionItem(Publicacion("Pepe", "madre flipa que pedazo de descripcion me estoy marcando con el tate"
                ,"2000-01-01", "Madrid", 10, 1, true, null, null, ArrayList<Comentario>(), ArrayList<MeGusta>()))
        )
        publicacionList.add(
            PublicacionItem(Publicacion("Pepe", "madre flipa que pedazo de descripcion me estoy marcando con el tate"
                ,"2000-01-01", "Madrid", 10, 1, true, null, null, ArrayList<Comentario>(), ArrayList<MeGusta>()))
        )
        publicacionList.add(
            PublicacionItem(Publicacion("Pepe", "madre flipa que pedazo de descripcion me estoy marcando con el tate"
                ,"2000-01-01", "Madrid", 10, 1, true, null, null, ArrayList<Comentario>(), ArrayList<MeGusta>()))
        )
        recyclerHome.adapter = PublicacionAdapter(publicacionList)
        recyclerHome.layoutManager = LinearLayoutManager(requireContext())
        recyclerHome.setHasFixedSize(true)
    }

}

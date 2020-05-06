package com.example.insta.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.insta.R
import com.example.insta.utils.MiSharedPreferences
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        view.txtSeguidoresProfile.text = MiSharedPreferences.getPreferencesString("n_seguidores")
        view.txtSeguidosProfile.text = MiSharedPreferences.getPreferencesString("n_seguidos")
        view.txtDescripcionProfile.text = MiSharedPreferences.getPreferencesString("descripcion")
        view.txtNombreProfile.text = MiSharedPreferences.getPreferencesString("nombre")
        view.topAppBarProfile.title = MiSharedPreferences.getPreferencesString("username")
        view.imgUserProfile.setImageResource(R.drawable.destello)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

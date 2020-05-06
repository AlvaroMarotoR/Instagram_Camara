package com.example.insta.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_logged.*

object MiNavController {

    private lateinit var navController: NavController


    fun getNavController(): NavController{
        return navController
    }

    fun findNavController(view: View){
        navController = Navigation.findNavController(view)
    }
    fun findNavController(activity: Activity, viewId: Int){
        navController = Navigation.findNavController(activity, viewId)
    }
    fun setUpWithNavController(bottomnavigationView: BottomNavigationView){
        NavigationUI.setupWithNavController(bottomnavigationView, getNavController())
    }

    fun popBackStack(){
        navController.popBackStack()
    }
    fun navigate(id: Int){
        navController.navigate(id)
    }


}
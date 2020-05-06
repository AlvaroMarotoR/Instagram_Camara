package com.example.insta.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ui.NavigationUI
import com.example.insta.R
import com.example.insta.utils.MiNavController
import com.example.insta.utils.MiSharedPreferences
import kotlinx.android.synthetic.main.activity_logged.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        MiSharedPreferences.init(this)
        super.onCreate(savedInstanceState)
        if(MiSharedPreferences.getPreferencesString("token") != "") {
            setContentView(R.layout.activity_logged)
            MiNavController.findNavController(this, R.id.fragmentMain)
            MiNavController.setUpWithNavController(bottomNavigationView)
            //bottomNavigationView.itemIconTintList = null

        }else{
            setContentView(R.layout.activity_main)
        }
    }


}




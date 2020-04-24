package com.example.insta.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.insta.R
import com.example.insta.api.MiRetrofitBuilder
import com.example.insta.models.LoginUser
import com.example.insta.models.Token
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        //autenticar al usuario antes de crear la actividad
        /*if(true){
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
            finish()
        }*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        Log.d("DEBUG", "$activeNetwork , $isConnected")



    }

}


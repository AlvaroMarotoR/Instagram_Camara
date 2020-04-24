package com.example.insta.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.insta.R
import com.example.insta.api.MiRetrofitBuilder
import com.example.insta.models.LoginUser
import com.example.insta.models.Token
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentLogin : Fragment() {

    //atributo lateinit(inicializacion tardia) que nos permite navegar entre los fragmentos
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO comprobar si existe token guardado para ir al siguiente fragmento
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        btnLogin.setOnClickListener{
            if(txtUsername.text.isEmpty() || txtPassword.text.isEmpty()) {
                val toast = Toast.makeText(activity, "Rellena los campos", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else{
                Log.d("DEBUG", "${txtUsername.text}, ${txtPassword.text}")
                val data = MutableLiveData<String>()
                val token = MiRetrofitBuilder.apiService.loginUser(LoginUser(txtUsername.text.toString(), txtPassword.text.toString())).enqueue(
                    object : Callback<Token> {
                        override fun onFailure(call: Call<Token>?, t: Throwable?) {
                            Log.v("retrofit", "call failed")
                        }
                        override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                            if(response?.code()==200) data.value = response.body()?.token
                            else data.value = "no hay"
                            Log.d("DEBUG", "token ${data.value}")
                        }
                    })
            }
        }

        //Botón que provoca el cambio del fragmento login al fragmento registro.
        btnRegistro.setOnClickListener{
            val bundle = bundleOf("hola" to "hola")
            navController.navigate(R.id.action_fragmentLogin_to_fragmentRegister2, bundle)
        }

        //Botón para cambiar el fragmento login al fragmento registro
        btnCamera.setOnClickListener {
          navController.navigate(R.id.action_fragmentLogin_to_fragmentCamera)
        }

    }

}

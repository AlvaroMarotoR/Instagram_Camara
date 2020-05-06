package com.example.insta.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.insta.R
import com.example.insta.api.MiRetrofitBuilder
import com.example.insta.models.LoginUser
import com.example.insta.models.Token
import com.example.insta.models.User
import com.example.insta.utils.MiNavController
import com.example.insta.utils.MiSharedPreferences
import com.example.insta.utils.MiViewUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentLogin : Fragment() {

    //variable estatica publica para mostrar menesajes al usuario
    companion object{
        var registro = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO comprobar si existe token guardado para ir al siguiente fragmento
        //autenticar al usuario antes de crear la actividad

        Log.d("retrofit", MiSharedPreferences.getPreferencesString("email").toString())
        Log.d("retrofit", MiSharedPreferences.getPreferencesString("username").toString())
        Log.d("retrofit", MiSharedPreferences.getPreferencesString("nombre").toString())
        Log.d("retrofit", MiSharedPreferences.getPreferencesString("apellido1").toString())
        Log.d("retrofit", MiSharedPreferences.getPreferencesString("seguidos").toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        //añadimos estilo a los campos de texto
        MiViewUtils.changeEditTextIconColor(view.txtUsername, R.color.grisClaro, requireContext())
        MiViewUtils.changeEditTextIconColor(view.txtPassword, R.color.grisClaro, requireContext())
        changeTextField(view.txtUsername)
        changeTextField(view.txtPassword)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inicializamos el navigationcontroller para poder navegar entre fragmentos
        MiNavController.findNavController(view)
        //en caso de que hayamos registrado un usuario y sido redirigidos a este fragmento, mostramos un mensaje
        if(registro) MiViewUtils.changeText(R.string.txtEstadoSuccess, txtEstadoRegistroLogin); MiViewUtils.changeTextViewColor(txtEstadoRegistroLogin, R.color.negro, requireContext())
        //añadimos escucha al boton de login
        btnLogin.setOnClickListener{
            //comprobamos que los campos no esten vacios
            if(!txtUsername.text.isValid() || !txtPassword.text.isValid()) MiViewUtils.showToast(R.string.errorCamposRellenos, requireContext())
            else{
                //comprobamos que el formato del email sea correcto
                if(!txtUsername.text.isValidEmail()) MiViewUtils.showToast(R.string.errorEmailFormat, requireContext())
                else{
                    //iniciamos una corrutina en el hilo de entrada y salida para hacer peticion a la bd
                    CoroutineScope(IO).launch{
                        //mostramos informacion al usuario
                        MiViewUtils.showView(progressBarLogin)
                        MiViewUtils.hideView(txtEstadoRegistroLogin)
                        //y desactivamos el boton para no hacer mas peticiones
                        MiViewUtils.disableButton(btnLogin)
                        //realizamos la peticion y la encolamos
                        getToken().enqueue(
                            object : Callback<Token> {
                                //si la peticion falla
                                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                                    MiViewUtils.showToast(R.string.errorServerConnect, requireContext())
                                    MiViewUtils.hideView(progressBarLogin)
                                    MiViewUtils.enableButton(btnLogin)
                                }
                                //si obtenemos respuesta
                                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                                    //y el codigo es ok, podemos hacer la segunda peticion para obtener los datos
                                    if(response?.code()==200) {
                                        //en caso de que el body tenga un token no vacio
                                        response.body()?.token?.let {token->
                                            //lo añadimos a nuestros preferences
                                            MiSharedPreferences.editPreferencesString("token", token)
                                            //y hacemos otra peticion para obtener todos los datos del usuario logeado
                                            getUserLogged(token).enqueue(
                                                object : Callback<User>{
                                                    //Si falla la peticion mostramos mensaje al usuario
                                                    override fun onFailure(call: Call<User>, t: Throwable) {
                                                        MiViewUtils.showToast(R.string.errorServerConnect, requireContext())
                                                        MiViewUtils.enableButton(btnLogin)
                                                    }
                                                    //en caso de que el servidor responda
                                                    override fun onResponse(call: Call<User>, response: Response<User>) {
                                                        when(response.code()){
                                                            200->{
                                                                //captamos el usuario y lo guardamos en nuestras preferences
                                                                val user  = response.body()
                                                                MiSharedPreferences.editPreferencesString("email", user?.email.toString())
                                                                MiSharedPreferences.editPreferencesString("username", user?.username.toString())
                                                                MiSharedPreferences.editPreferencesString("nombre", user?.nombre.toString())
                                                                MiSharedPreferences.editPreferencesString("apellido1", user?.apellido1.toString())
                                                                MiSharedPreferences.editPreferencesString("apellido2", user?.apellido2.toString())
                                                                MiSharedPreferences.editPreferencesString("telefono", user?.telefono.toString())
                                                                MiSharedPreferences.editPreferencesString("imagen", user?.imagen.toString())
                                                                MiSharedPreferences.editPreferencesString("genero", user?.genero.toString())
                                                                MiSharedPreferences.editPreferencesString("n_seguidores", user?.n_seguidores.toString())
                                                                MiSharedPreferences.editPreferencesString("n_seguidos", user?.n_seguidos.toString())
                                                                MiSharedPreferences.editPreferencesString("descripcion", user?.descripcion.toString())
                                                                //para almacenar array lo convertimos en json
                                                                val gson = Gson()
                                                                user?.seguidos?.let { it ->
                                                                    val jsonSeguidos = gson.toJson(it)
                                                                    MiSharedPreferences.editPreferencesString("seguidos", jsonSeguidos)
                                                                }
                                                                user?.seguidores?.let { it ->
                                                                    val jsonSeguidores = gson.toJson(it)
                                                                    MiSharedPreferences.editPreferencesString("seguidores", jsonSeguidores)
                                                                }
                                                                MiViewUtils.hideView(progressBarLogin)
                                                                requireActivity().setContentView(R.layout.activity_logged)
                                                            }
                                                            //si la peticion es incorrecta, informamos al usuario
                                                            401->{
                                                                MiViewUtils.hideView(progressBarLogin)
                                                                MiViewUtils.enableButton(btnLogin)
                                                                MiViewUtils.changeText(R.string.errorLoginCredentials, txtEstadoRegistroLogin); MiViewUtils.changeTextViewColor(txtEstadoRegistroLogin, R.color.errorRed, requireContext())
                                                                MiViewUtils.showView(txtEstadoRegistroLogin)
                                                            }
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    //si la consulta es erronea, informamos al usuario
                                    else {
                                        MiViewUtils.changeText(R.string.errorLoginCredentials, txtEstadoRegistroLogin); MiViewUtils.changeTextViewColor(txtEstadoRegistroLogin, R.color.errorRed, requireContext())
                                        MiViewUtils.hideView(progressBarLogin)
                                        MiViewUtils.enableButton(btnLogin)
                                        MiViewUtils.showView(txtEstadoRegistroLogin)
                                    }
                                }
                            })
                    }
                }

            }
        }
        //nos lleva al fragment de registro
        btnRegistro.setOnClickListener{
            CoroutineScope(Main).launch { gotoRegisterFragment() }
        }

        btnCamera.setOnClickListener{
            CoroutineScope(Main).launch { gotoCameraFragment() }
        }
    }


    //funcion que nos lleva al fragment de registro
    private fun gotoRegisterFragment(){
        MiNavController.navigate(R.id.action_fragmentLogin_to_fragmentRegister2)
    }

    private fun gotoCameraFragment(){
        MiNavController.navigate(R.id.action_fragmentLogin_to_fragmentCamera)
    }
    //funciones de peticiones al servidor
    private fun getToken(): Call<Token>{
        return MiRetrofitBuilder.apiService.loginUser(LoginUser(txtUsername.text.toString(), txtPassword.text.toString()))
    }

    private fun getUserLogged(token: String): Call<User>{
        return MiRetrofitBuilder.apiService.getUserLogged("Token $token")
    }

    //metodo para comprobar el formato de email
    private fun CharSequence.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    private fun CharSequence.isValid() = !isNullOrEmpty()

    //funcion para añadir escucha a los campos de texto
    private fun changeTextField(txt: EditText){
        txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(txt.text.isEmpty()){
                    MiViewUtils.changeEditTextIconColor(txt, R.color.grisClaro, requireContext())
                } else{
                    MiViewUtils.changeEditTextIconColor(txt, R.color.azulClaro, requireContext())
                }

            }
            //capturamos si entra un espacio para eliminarlo
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textEntered = txt.text.toString()

                if (textEntered.isNotEmpty() && textEntered.contains(" ")) {
                    txt.setText(txt.text.toString().replace(" ", ""))
                    txt.setSelection(txt.text!!.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }
        })
    }

}

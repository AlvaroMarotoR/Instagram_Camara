package com.example.insta.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.insta.R
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.configuration.UpdateConfiguration
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import kotlinx.coroutines.newSingleThreadContext
import java.io.File
import java.net.URI


/**
 * A simple [Fragment] subclass.
 */
class FragmentCamera : Fragment() {

    //Array de permisos que pediremos al usuario (cámra, poder guardar y cargar archivos  en el teléfono)
    val permisos = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    //Directorio de prueba
    val directorioSD = Environment.getExternalStorageDirectory()
    //Destino de prueba
    val destino = File(directorioSD,"hola")

    //VARIABLES QUE SE USAN EN LA LIBRERÍA FOTOAPPARAT
    //Instanciamos el objeto FotoApparat
    val fotoapparat =  Fotoapparat(
        context = context!! ,
        view = camera_view,                   // view which will draw the camera preview
        scaleType = ScaleType.CenterCrop,    // (optional) we want the preview to fill the view
        lensPosition = back(),               // (optional) we want back camera
        cameraConfiguration = CameraConfiguration(), // (optional) define an advanced configuration
        logger = loggers(                    // (optional) we want to log camera events in 2 places at once
            logcat(),                   // ... in logcat
            fileLogger(requireContext())            // ... and to file
        ),
        cameraErrorCallback = { error -> "Error tremendo" }   // (optional) log fatal errors
    )


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?  {
        val pruebas = inflater.inflate(R.layout.fragment_camera, container, false);
        // Inflate the layout for this fragment
        return pruebas;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        fab_camera.setOnClickListener {
            hacerFoto()
        }

        fab_switch_camera.setOnClickListener {
            cambiarCamara()
        }

        fab_flash.setOnClickListener {
            cambiarFlash()


        }

    }






    //Función que devuelve booleano, y vemos si el usuario tiene esos permisos o no.
    private fun tienePermisos() : Boolean {
        //Igualamos los permisos que pedimos, con los que tiene el teléfono, así devuelve true o false.
        return ContextCompat.checkSelfPermission(activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity!!,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    //Función que nos pide esos permisos, para ello utilizamos el array creado anteriormente.
    fun requierePermisos(){
        ActivityCompat.requestPermissions(
            activity!!,
             permisos,
            0
        )
    }


    //Función para parar la cámara.
    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
    }


    //Función para iniciar la cámara.
    override fun onStart() {
        super.onStart()

        //Si no tiene permisos, se los pedimos.
        if( tienePermisos() ){
            requierePermisos()
        //Si los tiene, se inicia la cámara.
        } else {
            fotoapparat.start()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }



    //Función que realiza la foto
    private fun hacerFoto(){
        //Si no tiene permisos, se le pide al usuario.
        if( tienePermisos() ) {
            requierePermisos()
        } else {
            val foto = fotoapparat.takePicture()
            // Asynchronously saves photo to file
            foto.saveToFile(destino)
        }
    }
    //Función para cambiar la cámara
    private fun cambiarCamara(){
    }

    private fun cambiarFlash(){

    }





}

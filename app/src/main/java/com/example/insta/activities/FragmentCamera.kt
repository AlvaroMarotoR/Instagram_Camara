package com.example.insta.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.insta.R
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class FragmentCamera : Fragment() {

    //Array de permisos que pediremos al usuario (cámra, poder guardar y cargar archivos  en el teléfono)
    val permisos = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    var fotoapparat: Fotoapparat? = null
    val directorioSD = Environment.getExternalStorageDirectory()
    val destino = File(directorioSD,"hola")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
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

    //Función que "Crea" la cámara, isntanciandola con unos valores por defecto.
    private fun crearFotoapparat(){
        val cameraView = view!!.findViewById<CameraView>(R.id.camera_view)

        fotoapparat = Fotoapparat(activity!!, view = cameraView, scaleType = ScaleType.CenterCrop, lensPosition = back(), logger = loggers(
            logcat()), cameraErrorCallback = { error -> println("Error al grabar: $error") }
        )
    }

    //Función para parar la cámara.
    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
    }

    //Función para iniciar la cámara.
    override fun onStart() {
        super.onStart()

        //Si no tiene permisos, se los pedimos.
        if( tienePermisos() ){
            requierePermisos()
        //Si los tiene, se inicia la cámara.
        } else {
            fotoapparat?.start()
        }
    }

    //Función que realiza la foto
    private fun hacerFoto(){
        //Si no tiene permisos, se le pide al usuario.
        if( tienePermisos() ) {
            requierePermisos()
        } else {
            fotoapparat?.takePicture()?.saveToFile(destino)
        }
    }





}

package com.example.insta.activities

import android.Manifest
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.insta.R

import com.priyankvasa.android.cameraviewex.AspectRatio
import com.priyankvasa.android.cameraviewex.ErrorLevel
import com.priyankvasa.android.cameraviewex.Image
import com.priyankvasa.android.cameraviewex.Modes
import com.priyankvasa.android.cameraviewex.Modes.Flash.Companion.FLASH_ON
import com.priyankvasa.android.cameraviewex.VideoSize
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
 class FragmentCamera : Fragment(), CoroutineScope {
    //VARIABLES
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    private val parentDir: String
            by lazy { "${Environment.getExternalStorageDirectory().absolutePath}/Carpeta_Pruebas" }

    private val imageOutputDirectory: String by lazy { "$parentDir/images".also { File(it).mkdirs() } }

    private val nextImageFile: File
        get() = File(imageOutputDirectory, "image_${System.currentTimeMillis()}.jpg")

    //private val videoOutputDirectory: String by lazy { "$parentDir/videos".also { File(it).mkdirs() } }
    private lateinit var videoFile: File

    @SuppressLint("MissingPermission") //Nos permite saltarnos los warnings posibles por falta de permisos.
    private val imageCaptureListener: View.OnClickListener = View.OnClickListener { camera.capture() }

    @SuppressLint("MissingPermission")
    private val videoCaptureListener: View.OnClickListener = View.OnClickListener {
        //Si la cámara está grabando y se da al botón, se para el video.
        if (camera.isVideoRecording){
            camera.stopVideoRecording() }
        //Si en cambio, la cámara está pausada, deberemos de iniciarla.
        else {
            camera.startVideoRecording(videoFile){
                videoFrameRate = 30 //FPS a los que se graba el video.
                maxDuration = 10000 // Duración máxima del video de 10s.
                videoStabilization = true //Permitir la estabilización del video.
                videoSize  = VideoSize.Max
            }

        }
    }
    //Array de Permisos
    companion object {
        val TAG: String = FragmentCamera::class.java.run { canonicalName ?: name }

        private val permisosApp: Array<String> = arrayOf(
            CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        fun newInstance(): FragmentCamera = FragmentCamera()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? = inflater.inflate(R.layout.fragment_camera, container, false);



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraCamara()
        configuraVista()
    }

    override fun onResume() {
        super.onResume()
        actualizaEstadoVista()
        //Mediante la función de los permisos.
        verPermisos().let {
            if (it.isEmpty()) //Si ese array está vacio, quiere decir que se tienen todos los permisos.
                camera.start() //Se comienza la cámara
            else
                requestPermissions(it, 1) } //En caso de que ese array no este vacio y falte algún permisos, se pide.
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        //cameraPreviewFrameHandler.release()
        camera.destroy()
        job.cancel()
        super.onDestroyView()
    }

    private fun configuraCamara(){

        //Con la cámara
        with(camera){
            addCameraOpenedListener { Timber.i("Cámara abierta") } //Notificamos que se abre la cámara, al hilo pricipal.
            //setContinuousFrameListener { cameraPreviewFrameHandler.frameRate } //Callback que notifica que la cámara está abierta en el hilo secundari.
            addPictureTakenListener { image: Image -> launch {guardarArchivo(image) }}
            addCameraErrorListener{ t, errorLevel ->
                when (errorLevel) {
                    ErrorLevel.Error -> Timber.e(t)
                    ErrorLevel.ErrorCritical -> Timber.w(t)
                }
            }

            /*
            Aqui irían los Listeners de Abrir y Cerrar la cámara
             */

            //Creo un listener si la cámara se cierra
            addCameraClosedListener { Timber.i("Se ha cerrado la cámara.") }
        }
    }

    private fun configuraVista(){

        //FLASH
        @DrawableRes val flashDrawableId: Int

        fab_flash.setOnClickListener {
            camera.flash = when (camera.flash) {
                Modes.Flash.FLASH_OFF -> {
                    Modes.Flash.FLASH_AUTO
                }
                Modes.Flash.FLASH_AUTO -> {
                    FLASH_ON
                }
                FLASH_ON -> {
                    Modes.Flash.FLASH_OFF
                }
                else -> return@setOnClickListener
            }
            //fab_flash.setImageDrawable(ActivityCompat.getDrawable(context!!,flashDrawableId))
        }


    }

    private fun actualizaEstadoVista(){
        if (camera.isSingleCaptureModeEnabled){
            fab_camera.setOnClickListener(imageCaptureListener)
        }
    }



    //FUNCIONES DE LA CÁMARA
    private fun cambiaCamara(){
        camera.facing =
            when (camera.facing) {
                Modes.Facing.FACING_BACK -> Modes.Facing.FACING_FRONT

            else ->
                Modes.Facing.FACING_BACK
        }
    }

    //FUNCIÓN PARA REALIZAR FOTOD
    private fun hacerFoto(){
        fab_camera.setOnClickListener(imageCaptureListener)
    }







    //Función de prueba para guardar las fotos en un archivo nuevp.
    private suspend fun guardarArchivo(image: Image): File {
        //Creamos una variable que ns servirá para crar un nuevo archivo
        val salida: File = nextImageFile.apply { createNewFile() }

        runCatching {
            withContext(Dispatchers.IO){
                BufferedOutputStream(salida.outputStream()).use { it.write(image.data) }
            }
        }
            .onFailure {
                context?.toast("No se ha podido guardar la foto.")
                Timber.e(it)
            }
            .onSuccess { context?.toast("Foto guardada en la direccion ${salida.absolutePath}")}
        return salida
    }




    //Función de Array de Strings (tipo de los permisos)
    // Finalidad: Igualar los permisos que tiene el teléfono con los que pide la app (mediante .filter) e ir eliminandolos de ese ArrayList.
    private fun verPermisos(): Array<String> {
        val context: Context = context?: return permisosApp

        //El método filter permite filtrar los elementos del array (los permisos), mediante
        //la condición escrita dentro de las llaves, en este caso, los permisos que aun no se hayan dado.
        return permisosApp
            .filter { ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }
            .toTypedArray() //Retorna un array, conteniendo los elementos que pasen el filtro.
    }


}

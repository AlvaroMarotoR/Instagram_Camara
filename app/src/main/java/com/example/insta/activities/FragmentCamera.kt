package com.example.insta.activities

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.util.Rational
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.insta.R
import com.example.insta.utils.MiViewUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.Serializable
import java.util.concurrent.Executor


/**
 * A simple [Fragment] subclass.
 */
 class FragmentCamera: Fragment(), Executor {

    //atributo lateinit(inicializacion tardia) que nos permite navegar entre los fragmentos
    private lateinit var navController: NavController

    private var REQUEST_CODE_PERMISSIONS: Int = 101 //Código que identifica a los permisos.
    private var REQUIRED_PERMISSIONS: Array<String> = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, RECORD_AUDIO) //Permisos

    private var lensFacing = CameraX.LensFacing.FRONT

    private var modoFoto: Boolean = true //Booleano para hacer video o foto.



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (permisosDados()) {
            textureView.post { empezarCamara() }
        } else {
            this.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        capture_button.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }

        btnCamera.setOnClickListener { swapCamera() }


    }


    /*
    FUNCIÓN PARA COMENZAR LA CÁMARA Y QUE SE PUEDA VER LA PREVIA
     */
    @SuppressLint("RestrictedApi")
    private fun empezarCamara() {
        CameraX.unbindAll()

        //CASO PRÁCTICO DE VISTA PREVIA DE LA CÁMARA
        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)//Se comenzará con la cámara trasera-
            setTargetAspectRatioCustom(Rational(textureView.width, textureView.height))
            setTargetResolution(Size(textureView.width, textureView.height))
        }.build()

        val preview = Preview(previewConfig)


        preview.setOnPreviewOutputUpdateListener {
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)
            textureView.surfaceTexture = it.surfaceTexture
            updateTransform()
        }


        //CASO PRÁCTICO DE TOMA DE FOTOS
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetAspectRatioCustom(Rational(textureView.width, textureView.height))
            setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY) //Modo de captura en máxima calidad.
        }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)



        btnFlash.setOnClickListener {


            if ((imageCapture.flashMode == FlashMode.OFF)) {
                imageCapture.flashMode = FlashMode.ON
                btnFlash.setBackgroundResource(R.drawable.flash_encendido)

            } else {
                imageCapture.flashMode = FlashMode.OFF
                btnFlash.setBackgroundResource(R.drawable.flash_apagado)
            }


        }

        capture_button.setOnClickListener {
            modoFoto = true


            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "imagenesguardadas" + File.separator
            )


            imageCapture.takePicture(file, this, object : ImageCapture.OnImageSavedListener { //Méetodo proporcionado por el uso del caso práctico de ImageCapture

                    @SuppressLint("ResourceType")
                    override fun onImageSaved(file: File) {

                        val uri = file.absolutePath
                        val frg: Fragment = FragmentCamera2()
                        val fm: FragmentManager = activity!!.supportFragmentManager
                        val bundle: Bundle = Bundle()

                        bundle.putString("foto",uri)
                        bundle.putString("face", lensFacing.toString())



                        frg.arguments = bundle

                        navController.navigate(R.id.action_fragmentCamera3_to_fragmentCamera22, bundle)

                    }

                    override fun onError(
                        useCaseError: ImageCapture.ImageCaptureError,
                        message: String,
                        cause: Throwable?
                    ) {
                        MiViewUtils.showToast( "La foto no ha podido ser guardada", requireContext())
                        cause?.printStackTrace()
                    }
                }
            )

        }


        //CASO PRÁCTICO DE GRABACIÓN DE VIDEO
        val videoCaptureConfig = VideoCaptureConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetAspectRatioCustom(Rational(textureView.width, textureView.height))
            setTargetRotation(textureView.display.rotation)
        }.build()

        val videoCapture = VideoCapture(videoCaptureConfig)

        /*capture_button.setOnLongClickListener {

            val video = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "${System.currentTimeMillis()}.mp4"
            )


            videoCapture.startRecording(
                video,
                this,
                object : VideoCapture.OnVideoSavedListener {

                    override fun onVideoSaved(file: File) {
                        Log.i(javaClass.simpleName, "Video File : $video")
                    }

                    override fun onError(
                        videoCaptureError: VideoCapture.VideoCaptureError,
                        message: String,
                        cause: Throwable?
                    ) {
                        Log.i(javaClass.simpleName, "Video Error: $message")
                    }


                })

            true

        };*/

        if (modoFoto) {
            CameraX.bindToLifecycle(this, preview, imageCapture) /* For Preview and image Capture */
        } else CameraX.bindToLifecycle(this, preview, videoCapture)

    }


    @SuppressLint("RestrictedApi")
    private fun swapCamera() {

        lensFacing =
            when (lensFacing) {
                CameraX.LensFacing.BACK -> CameraX.LensFacing.FRONT
                CameraX.LensFacing.FRONT -> CameraX.LensFacing.BACK

                else -> CameraX.LensFacing.BACK
            }
        CameraX.getCameraWithLensFacing(lensFacing)
        empezarCamara()
    }


    private fun updateTransform() {
        val matrix = Matrix()
        // Compute the center of the view finder
        val centerX = textureView.getMeasuredWidth() / 2f
        val centerY = textureView.getMeasuredHeight() / 2f

        // Correct preview output to account for display rotation
        val rotationDegree: Int;
        val rotation: Int = textureView.getRotation().toInt()

        when (rotation) {
            Surface.ROTATION_0 -> rotationDegree = 0
            Surface.ROTATION_90 -> rotationDegree = 90
            Surface.ROTATION_180 -> rotationDegree = 180
            Surface.ROTATION_270 -> rotationDegree = 270
            else -> return
        }
        matrix.postRotate(rotationDegree.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        textureView.setTransform(matrix)
    }



    /*
    Si los permisos están dados, comienza la cámara, si no, se informa al usuario.
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (permisosDados()) {
                textureView.post { empezarCamara() }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se han otorgado los permisos suficientes.",
                    Toast.LENGTH_SHORT
                ).show()
                //finish()
            }
        }
    }


    /*
    Si los permisos están dados o no
     */
    private fun permisosDados(): Boolean {
        for (permisos: String in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permisos
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun execute(command: Runnable) {
        command.run()
    }
}










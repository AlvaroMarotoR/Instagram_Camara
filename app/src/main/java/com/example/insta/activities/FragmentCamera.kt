package com.example.insta.activities

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.util.Rational
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.insta.R
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File



/**
 * A simple [Fragment] subclass.
 */
class FragmentCamera: Fragment() {

    private var REQUEST_CODE_PERMISSIONS: Int = 101;
    private var REQUIRED_PERMISSIONS: Array<String> = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    private var lensFacing: CameraX.LensFacing = CameraX.LensFacing.FRONT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(permisosDados()){
            empezarCamara()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS , REQUEST_CODE_PERMISSIONS)
        }

        // Every time the provided texture view changes, recompute layout
        capture_button.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            updateTransform()
        }

        btnCamera.setOnClickListener{swapCamera()}
    }


    /*
    FUNCIÓN PARA COMENZAR LA CÁMARA Y QUE SE PUEDA VER LA PREVIA
     */
    @SuppressLint("RestrictedApi")
    private fun empezarCamara(){
        CameraX.unbindAll()
        //CASO PRÁCTICO DE VISTA PREVIA DE LA CÁMARA
        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing) //Se comenzará con la cámara trasera-
            setTargetAspectRatio(Rational(textureView.width, textureView.height))
            setTargetResolution(Size(textureView.width, textureView.height))
        }.build()

        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {
            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView , 0)
            textureView.surfaceTexture = it.surfaceTexture
            updateTransform()
        }


        //CASO PRÁCTICO DE TOMA DE FOTOS
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply{
            setLensFacing(lensFacing)
            setTargetAspectRatio(Rational(textureView.width, textureView.height))
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY) //Modo de captura en máxima calidad.
        }.build()

         val imageCapture = ImageCapture(imageCaptureConfig)

        capture_button.setOnClickListener {

            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file,object :ImageCapture.OnImageSavedListener{ //Méetodo proporcionado por el uso del caso práctico de ImageCapture
                override fun onImageSaved(file: File) {
                    Toast.makeText(requireContext(), "Foto guardada en: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                }

                override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                   Toast.makeText(requireContext(), "Error al guardar la foto.", Toast.LENGTH_SHORT).show()
                    cause?.printStackTrace()
                }
            })
        }
        CameraX.bindToLifecycle(this,preview,imageCapture) /* For Preview and image Capture */
    }


    @SuppressLint("RestrictedApi")
    private fun swapCamera(){
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
        val centerX =textureView.getMeasuredWidth()/2f
        val centerY =textureView.getMeasuredHeight()/2f

        // Correct preview output to account for display rotation
        val rotationDegree: Int;
        val rotation: Int = textureView.getRotation().toInt()

            when(rotation){
            Surface.ROTATION_0 -> rotationDegree = 0
            Surface.ROTATION_90 -> rotationDegree = 90
            Surface.ROTATION_180 -> rotationDegree = 180
            Surface.ROTATION_270 -> rotationDegree = 270
            else -> return
        }
        matrix.postRotate(rotationDegree.toFloat(),centerX,centerY)

        // Finally, apply transformations to our TextureView
        textureView.setTransform(matrix)
    }

    /*
    Si los permisos están dados, comienza la cámara, si no, se informa al usuario.
     */

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if ( permisosDados() ) {
                textureView.post{ empezarCamara() }
            } else {
                Toast.makeText(requireContext(), "No se han otorgado los permisos suficientes.", Toast.LENGTH_SHORT).show()
                //finish()
            }
        }
    }


    /*
    Si los permisos están dados o no
     */
    private fun permisosDados(): Boolean {
        for (permisos: String in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permisos) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}






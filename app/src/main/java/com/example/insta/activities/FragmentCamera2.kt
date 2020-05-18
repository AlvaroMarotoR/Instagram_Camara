package com.example.insta.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.Toast
import androidx.camera.core.CameraX
import androidx.core.view.ViewCompat.setScaleY

import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.example.insta.R
import kotlinx.android.synthetic.main.fragment_camera2.*

import java.io.File


/**
 * A simple [Fragment] subclass.
 */
 class FragmentCamera2 : Fragment() {

    val fragmentoBueno = FragmentCamera()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera2, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val fotoPath: String = arguments?.getString("foto").toString()
        val facing: String = arguments?.getString("face").toString()


        val bitmap = BitmapFactory.decodeFile(File(fotoPath).toString())
        //val bitMapRotated: Bitmap = Bitmap.createBitmap(bitMapEscalado, 0 ,0 , bitMapEscalado.width, bitMapEscalado.height, matrix, true


        if(facing.equals("BACK"))
        {
            imageView.setImageBitmap(rotacNecesario(bitmap,fotoPath))
        }

        if(facing.equals("FRONT"))
        {
            val cx = bitmap.width.toFloat()
            val cy = bitmap.height.toFloat()
            val flippedBitmap = bitmap.flip(-1f, -1f, cx, cy)
            imageView.setImageBitmap(rotacNecesario(bitmap,fotoPath))

        }


    }

    private  fun rotacNecesario(img: Bitmap, imgPath: String): Bitmap {
        val  ei = ExifInterface(imgPath)

        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotarImagen(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotarImagen(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotarImagen(img, 270)

            else -> return img
        }
    }


    private fun rotarImagen(img: Bitmap, grados: Int): Bitmap{
        val matrix = Matrix()
        matrix.postRotate(grados.toFloat())

        return Bitmap.createBitmap(img, 0 , 0 , img.width, img.height, matrix, true)
    }


    private fun Bitmap.flip(x: Float, y: Float, cx: Float, cy: Float): Bitmap {
        val matrix = Matrix().apply { postScale(x, y, cx, cy) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }




}

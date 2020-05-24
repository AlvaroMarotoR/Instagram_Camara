package com.example.insta.activities

import android.R.attr.button
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.insta.R
import kotlinx.android.synthetic.main.drawing_room.*
import kotlinx.android.synthetic.main.fragment_camera2.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * A simple [Fragment] subclass.
 */

class FragmentCamera2 : Fragment()
{

    lateinit var path: Path



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_camera2, container, false)

    }

    @SuppressLint("RestrictedApi", "ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        //Obtenemos los datos pasados por el bundle.
        val fotoPath: String = arguments?.getString("foto").toString()
        val facing: String = arguments?.getString("face").toString()

        //Obtenemos la imagen y se guarda en Bitmap.
        val bitmap = BitmapFactory.decodeFile(File(fotoPath).toString())



        if(facing == "BACK")
        {
            Glide.with(imageView).load(rotacNecesario(bitmap,fotoPath)).into(imageView)
        }

        if(facing == "FRONT")
        {
            val cx = bitmap.width.toFloat()
            val cy = bitmap.height.toFloat()
            val flippedBitmap = bitmap.flip(-1f, -1f, cx, cy)
            Glide.with(imageView).load(rotacNecesario(bitmap,fotoPath)).into(imageView)
        }



        val vistaEditar = DrawingView(requireContext())

        activity?.setContentView(R.layout.drawing_room)

        val mDrawingPad = activity?.findViewById<LinearLayout>(R.id.view_drawing_pad)
        val drawableBit = BitmapDrawable(requireContext().resources, rotacNecesario(bitmap,fotoPath))
        Glide.with(imageView).load(drawableBit).into(imageView)


        //GUARDAR FOTO
        val guardaBoton = activity?.findViewById<Button>(R.id.btnSave)
        guardaBoton?.setOnClickListener {

            mDrawingPad?.isDrawingCacheEnabled = true
            val bit: Bitmap? = mDrawingPad?.drawingCache

            val ruta: String = Environment.getExternalStorageDirectory().toString()
            val directorio = File(ruta + "/Flash")
            directorio.mkdirs()


            val n: Calendar = Calendar.getInstance()
            val nombreFoto = "${n.timeInMillis}.jpg";
            val archivo = File(directorio, nombreFoto)

            //Mostramos el path donde se ha guardado el View.
            val s: String = archivo.absolutePath
            Toast.makeText(this.context, "Archivo guardado en $s", Toast.LENGTH_SHORT).show()



            try {
                val output = FileOutputStream(archivo)
                bit?.compress(Bitmap.CompressFormat.JPEG, 90, output)
                output.flush()
                output.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        mDrawingPad?.background = drawableBit
        mDrawingPad?.addView(vistaEditar)








}

private  fun rotacNecesario(img: Bitmap, imgPath: String): Bitmap
    {
        val  ei = ExifInterface(imgPath)

        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL))
        {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotarImagen(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotarImagen(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotarImagen(img, 270)

            else -> return img
        }
    }


    private fun rotarImagen(img: Bitmap, grados: Int): Bitmap
    {
        val matrix = Matrix()
        matrix.postRotate(grados.toFloat())
        return Bitmap.createBitmap(img, 0 , 0 , img.width, img.height, matrix, true)
    }


    private fun Bitmap.flip(x: Float, y: Float, cx: Float, cy: Float): Bitmap
    {
        val matrix = Matrix().apply { postScale(x, y, cx, cy) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }



}






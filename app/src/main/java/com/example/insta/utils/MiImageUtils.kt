package com.example.insta.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.*

object MiImageUtils {

    fun uriToFile(uri: Uri, name: String, context: Context): File{
        //lo transformamos en bitmap, dependiendo de la version del cliente
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        //creamos el ficheo en formato jpg
        val file = File(context.cacheDir, name)
        file.createNewFile()
        //creamos el array de bytes para transformar el bitmap en array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapdata = bos.toByteArray()
        val fos : FileOutputStream?
        //y lo escribimos en nuestro nuevo fichero
        try{
            fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch(e: FileNotFoundException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return file
    }

}
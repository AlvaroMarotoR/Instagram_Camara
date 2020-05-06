package com.example.insta.utils

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

//clase estatica que ofrece metodos para cambiar la UI dentro de corrutinas
object MiViewUtils {

    fun hideView(view: View) = CoroutineScope(Main).launch{ view.visibility = View.INVISIBLE }
    fun showView(view: View) = CoroutineScope(Main).launch{ view.visibility = View.VISIBLE }

    fun disableButton(btn: Button) = CoroutineScope(Main).launch{ btn.isEnabled = false }
    fun enableButton(btn: Button) = CoroutineScope(Main).launch{ btn.isEnabled = true }


    fun changeText(id: Int, view: TextView) = CoroutineScope(Main).launch{ view.setText(id) }
    fun changeText(txt: String, view: TextView) = CoroutineScope(Main).launch{ view.text = txt }
    fun changeTextViewColor(view: TextView, id: Int, context: Context) = CoroutineScope(Main).launch { view.setTextColor(context.resources.getColor(id, context.theme)) }
    fun changeEditTextColor(view: EditText, id: Int, context: Context) = CoroutineScope(Main).launch { view.setTextColor(context.resources.getColor(id, context.theme)) }
    fun changeEditTextIconColor(view: EditText, id: Int, context: Context) = CoroutineScope(Main).launch { view.compoundDrawableTintList = context.resources.getColorStateList(id, context.theme) }

    fun showToast(txt: String, context: Context) = CoroutineScope(Main).launch{ Toast.makeText(context, txt, Toast.LENGTH_SHORT).show() }
    fun showToast(id: Int, context: Context) = CoroutineScope(Main).launch{ Toast.makeText(context, id, Toast.LENGTH_SHORT).show() }

}
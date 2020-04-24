package com.example.insta.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.content.ContextCompat

import com.example.insta.R
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*

class FragmentRegister : Fragment() {


       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var sad :String

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        //crear y añadir el adaptador al spinner
        ArrayAdapter.createFromResource(
            context!!, R.array.genres_array , R.layout.custom_spinner
        ).also { adapter->
            adapter.setDropDownViewResource(R.layout.custom_spinner)
            view.txtGeneroRegister.adapter = adapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO cuando coja mes del picker ++

        changePasswordField(btnVerPassword1, txtPasswordRegister)
        changePasswordField(btnVerPassword2, txtPasswordRegister2)

    }

    private fun changePasswordField(checkBox: CheckBox, txt: EditText){
        checkBox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked){
                checkBox.buttonDrawable = ContextCompat.getDrawable(context!!, R.drawable.ic_visibility_off)
                txt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else{
                checkBox.buttonDrawable = ContextCompat.getDrawable(context!!, R.drawable.ic_visibility)
                txt.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
    }


}

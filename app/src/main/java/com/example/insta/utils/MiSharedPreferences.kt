package com.example.insta.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.insta.models.UserFollow
import kotlinx.coroutines.CoroutineScope

object MiSharedPreferences{

    private const val NAME = "USER_LOGGED"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(NAME, MODE)
    }

    //CLEAR DATASET
    fun clearPreferences(){
        sharedPreferences.edit().clear().apply()
    }
    //REMOVE A PAIR
    fun removePreference(key: String){
        sharedPreferences.edit().remove(key).apply()
    }

    //GET
    fun getPreferencesString(key: String):String?{
        return sharedPreferences.getString(key, "")
    }

    fun getPreferencesInt(key: String):Int{
        return sharedPreferences.getInt(key, 0)
    }

    fun getPreferencesFloat(key: String):Float{
        return sharedPreferences.getFloat(key, 0f)
    }

    fun getPreferencesBoolean(key: String):Boolean?{
        return sharedPreferences.getBoolean(key, false)
    }

    //EDIT
    fun editPreferencesString(key: String, value: String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun editPreferencesInt(key: String, value: Int){
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun editPreferencesFloat(key: String, value: Float){
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun editPreferencesBoolean(key: String, value: Boolean){
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun editPreferencesSet(key: String, value: Set<String>){
        sharedPreferences.edit().putStringSet(key, value).apply()
    }


}
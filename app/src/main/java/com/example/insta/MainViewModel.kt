package com.example.insta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.insta.models.Token
import com.example.insta.models.User
import com.example.insta.repository.Repository

class MainViewModel:ViewModel(){
    /*private val _token: MutableLiveData<Token> = MutableLiveData()

    val user: LiveData<User> = Transformations.switchMap(_token){
        Repository.getUser(Token("a5613cad2058f5926b71a16a3b2e0476d2072b81"))
    }
    fun setUser(token: Token){
        val update = token
        if(_token.value != update){
            _token.value = update
        }
    }

    fun cancelJobs(){
        Repository.cancelJobs()
    }*/
}
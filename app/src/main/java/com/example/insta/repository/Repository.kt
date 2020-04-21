package com.example.insta.repository

import androidx.lifecycle.LiveData
import com.example.insta.api.MiRetrofitBuilder
import com.example.insta.models.Token
import com.example.insta.models.User
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object Repository{
    /*var job: CompletableJob? = null
    fun getUser(token: Token): LiveData<User>{
        job = Job()
        return object: LiveData<User>(){
            override fun onActive() {
                super.onActive()
                job?.let { job->
                    CoroutineScope(IO + job).launch {
                        val user = MiRetrofitBuilder.apiService.getUserLogged(token.token)
                        withContext(Main){
                            value = user
                            job.complete()
                        }
                    }
                }
            }
        }
    }
    fun cancelJobs() = job?.cancel()*/
}
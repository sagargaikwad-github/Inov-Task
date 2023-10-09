package com.example.inov_task.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.inov_task.model.Attribute
import com.example.inov_task.model.LensCollection
import com.example.inov_task.retrofit.ApiService
import com.example.inov_task.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Array

class LensRepository {

    //Service initialized with init
    var apiService: ApiService

    init {
        apiService = RetrofitInstance().getRetrofitInstance().create(ApiService::class.java)
    }


    fun loadData(language: String, store: String): LiveData<LensCollection> {
        var data = MutableLiveData<LensCollection>()

        var lensList: LensCollection

        //Background Thread
        GlobalScope.launch(Dispatchers.IO) {
            val response = apiService.getLensData(language, store)

            if (response != null) {
                lensList = response.body()!!
                data.postValue(lensList)

                Log.i("RequestedData", " " + data.value)
            }
        }
        return data

    }

}
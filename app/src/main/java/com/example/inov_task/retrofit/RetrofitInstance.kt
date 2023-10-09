package com.example.inov_task.retrofit

import com.example.inov_task.util.Contants.baseURl
import de.hdodenhof.circleimageview.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    fun getRetrofitInstance(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseURl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
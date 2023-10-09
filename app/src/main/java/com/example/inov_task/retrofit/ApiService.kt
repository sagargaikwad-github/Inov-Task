package com.example.inov_task.retrofit

import com.example.inov_task.model.Attribute

import com.example.inov_task.model.LensCollection
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("rest/V1/productdetails/6701/253620")
    suspend fun getLensData(
        @Query("lang") language: String,
        @Query("store") store: String,
    ): Response<LensCollection>

}
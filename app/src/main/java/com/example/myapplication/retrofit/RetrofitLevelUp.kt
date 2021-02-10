package com.example.myapplication.retrofit

import android.content.Context
import com.example.myapplication.DATACLASS
import com.example.myapplication.RetrofitGet
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import com.example.myapplication.getHash
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitLevelUp {
    @FormUrlEncoded
    @POST("/test/levelup.php/")
    fun levelup(
            @Field("u_id") u_id : String,
            @Field("u_pw") u_pw : String,
            @Field("u_exp") u_exp : Int
    ): Call<DATACLASS>
}


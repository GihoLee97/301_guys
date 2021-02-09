package com.example.myapplication

import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitNickcheck {
    @FormUrlEncoded
    @POST("/test/nickcheck.php/")
    fun nickcheck(
        @Field("u_id") u_id: String,
        @Field("u_nickname") u_nickname: String)
            : Call<String>
}

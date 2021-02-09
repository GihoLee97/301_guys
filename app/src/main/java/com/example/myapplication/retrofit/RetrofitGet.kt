package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.lang.reflect.Type


interface RetrofitGet{
    @FormUrlEncoded
    @POST("/test/getall.php/")
    fun getall(
        @Field("u_id") u_id: String,
        @Field("u_pw") u_pw: String)
            : Call<DATACLASS>
}



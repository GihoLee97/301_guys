package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Retrofitservice{
    @FormUrlEncoded
    @POST("test/testlogin.php/")
    fun post_setidpw(
            @Field("u_id") u_id : String,
            @Field("u_pw") u_pw : String): Call<String>
}
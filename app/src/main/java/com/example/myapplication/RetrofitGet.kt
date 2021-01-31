package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitGet{
    @FormUrlEncoded
    @POST("test/call.php/")
    fun getdata(
        @Field("u_id") u_id: String,
        @Field("u_pw") u_pw: String,
        @Field("u_date") u_date: String)
    : Call<String>
}
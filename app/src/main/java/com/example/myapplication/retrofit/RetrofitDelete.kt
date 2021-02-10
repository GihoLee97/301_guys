package com.example.myapplication.retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitDelete {
    @FormUrlEncoded
    @POST("/test/delete.php/")
    fun retro_delete(
            @Field("u_id") u_id: String,
            @Field("u_pw") u_pw: String)
            : Call<String>
}
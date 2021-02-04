package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitChange {
    @FormUrlEncoded
    @POST("/test/changepw.php/")
    fun retro_changepw(
            @Field("u_id") u_id : String,
            @Field("u_pw") u_pw : String,
            @Field("new_pw") new_pw : String
            ): Call<String>
}
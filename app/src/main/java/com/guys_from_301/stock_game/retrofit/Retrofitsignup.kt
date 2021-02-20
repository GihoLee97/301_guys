package com.guys_from_301.stock_game

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Retrofitsignup{
    @FormUrlEncoded
    @POST("/test/Signup.php/")
    fun retro_signup(
        @Field("u_id") u_id: String,
        @Field("u_pw") u_pw: String,
        @Field("u_date") u_date: String)
    : Call<String>
}
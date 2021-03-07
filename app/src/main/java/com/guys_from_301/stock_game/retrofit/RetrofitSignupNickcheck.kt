package com.guys_from_301.stock_game.retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitSignupNickcheck  {
    @FormUrlEncoded
    @POST("/test/signupnickcheck.php/")
    fun signupnickcheck(
            @Field("u_id") u_id: String,
            @Field("u_pw") u_pw: String,
            @Field("u_date") u_date: String,
            @Field("u_nickname") u_nickname: String,
            @Field("u_imageNumber") u_imageNumber: Int)
    : Call<String>
}

package com._301_guys.stock_game

import retrofit2.Call
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

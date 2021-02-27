package com.guys_from_301.stock_game.retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitIdcheck {
    @FormUrlEncoded
    @POST("/test/idcheck.php/")
    fun idcheck(
            @Field("u_id") u_id: String)
            : Call<String>
}

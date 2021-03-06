package com.guys_from_301.stock_game.retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitFriendCheck {
    @FormUrlEncoded
    @POST("/test/friendcheck.php/")
    fun friendcheck(
            @Field("u_id") u_id: String)
            : Call<String>
}

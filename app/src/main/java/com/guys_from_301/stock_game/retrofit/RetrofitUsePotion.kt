package com.guys_from_301.stock_game

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitUsePotion {
    @FormUrlEncoded
    @POST("/test/usepotion.php/")
    fun setasset(
            @Field("u_id") u_id : String,
            @Field("u_money") u_money : Int,
            @Field("u_potion") u_potion : Int
    ): Call<String>
}
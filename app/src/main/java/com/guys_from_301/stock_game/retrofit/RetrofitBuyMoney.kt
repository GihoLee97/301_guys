package com.guys_from_301.stock_game

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitBuyMoney {
    @FormUrlEncoded
    @POST("/test/buymoney.php/")
    fun setasset(
            @Field("u_id") u_id : String,
            @Field("u_money") u_money : Int
    ): Call<String>
}
package com.guys_from_301.stock_game

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitSetAsset {
    @FormUrlEncoded
    @POST("/test/setasset.php/")
    fun setasset(
            @Field("u_id") u_id : String,
            @Field("u_setcash") u_setcash : Float,
            @Field("u_setmonthly") u_setmonthly : Float,
            @Field("u_setsalaryraise") u_setsalaryraise : Float,
            @Field("u_money") u_money : Int,
            @Field("u_potion") u_potion : Int
    ): Call<String>
}
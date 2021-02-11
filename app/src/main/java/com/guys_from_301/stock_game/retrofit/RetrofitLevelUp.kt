package com.guys_from_301.stock_game.retrofit

import com.guys_from_301.stock_game.DATACLASS
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitLevelUp {
    @FormUrlEncoded
    @POST("/test/levelup.php/")
    fun levelup(
            @Field("u_id") u_id : String,
            @Field("u_pw") u_pw : String,
            @Field("u_exp") u_exp : Int
    ): Call<DATACLASS>
}


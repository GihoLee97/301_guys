package com.guys_from_301.stock_game

import android.content.Context
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RetrofitGet{
    @FormUrlEncoded
    @POST("/test/getall.php/")
    fun getall(
        @Field("u_id") u_id: String,
        @Field("u_pw") u_pw: String)
            : Call<DATACLASS>
}

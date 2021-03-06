package com.guys_from_301.stock_game.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.DATACLASS
import com.guys_from_301.stock_game.DATACLASS_NOTICE
import com.guys_from_301.stock_game.RetrofitUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitNotice {
    @FormUrlEncoded
    @POST("/test/notice_get.php/")
    fun getnotice(
            @Field("u_id") u_id : String
    ): Call<MutableList<DATACLASS_NOTICE>>
}


package com.guys_from_301.stock_game

import android.content.Context
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProflieDB
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

fun getinformation(context: Context, u_id: String, u_pw: String) {
    val mContext : Context = context
    var profileDb : ProflieDB? = null
    var fungetinformation: RetrofitGet? = null
    val url = "http://stockgame.dothome.co.kr/test/getall.php/"
    var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
    //creating retrofit object
    var retrofit =
            Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
    //creating our api
    fungetinformation = retrofit.create(RetrofitGet::class.java)
    fungetinformation.getall(com.guys_from_301.stock_game.getHash(u_id).trim(), com.guys_from_301.stock_game.getHash(u_pw).trim()).enqueue(object : Callback<DATACLASS> {
        override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
//                println("---"+t.message)
        }
        override fun onResponse(call: Call<DATACLASS>, response: retrofit2.Response<DATACLASS>) {
            if (response.isSuccessful && response.body() != null) {
                var data: DATACLASS? = response.body()!!
                profileDb = ProflieDB?.getInstace(mContext)
                val newProfile = Profile()
                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                newProfile.nickname = data?.NICKNAME!!
                newProfile.history = data?.HISTORY!!
                newProfile.level = data?.LEVEL!!
                newProfile.exp = data?.EXP!!
                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                newProfile.money = data?.MONEY!!
                newProfile.profit = data?.PROFIT!!
                newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                profileDb?.profileDao()?.update(newProfile)
                profileDb = ProflieDB?.getInstace(mContext)
            }
        }
    })
}


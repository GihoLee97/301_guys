package com.example.myapplication.retrofit

import android.content.Context
import com.example.myapplication.DATACLASS
import com.example.myapplication.RetrofitGet
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import com.example.myapplication.getHash
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

fun funlevelup(context : Context, u_id : String, u_pw : String, u_exp:Int){
    val mContext : Context = context
    var profileDb : ProflieDB? = null
    var funlevel_up: RetrofitLevelUp? = null
    profileDb = ProflieDB.getInstace(mContext)
    val url = "http://stockgame.dothome.co.kr/test/levelup.php/"
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
    funlevel_up = retrofit.create(RetrofitLevelUp::class.java)

    funlevel_up.levelup(getHash(u_id).trim(), getHash(u_pw).trim(), u_exp).enqueue(object : Callback<DATACLASS> {
        override fun onFailure(call: Call<DATACLASS>, t: Throwable) {
//                println("---"+t.message)
        }
        override fun onResponse(call: Call<DATACLASS>, response: retrofit2.Response<DATACLASS>) {
            if (response.isSuccessful && response.body() != null) {
                var data : DATACLASS = response.body()!!
                profileDb = ProflieDB?.getInstace(mContext)
                val newProfile = Profile()
                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
                newProfile.history = profileDb?.profileDao()?.getHistory()!!
                newProfile.level = data?.LEVEL!!
                newProfile.exp = data?.EXP!!
                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                newProfile.money = profileDb?.profileDao()?.getMoney()!!
                newProfile.profit = profileDb?.profileDao()?.getProfit()!!
                newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                profileDb?.profileDao()?.update(newProfile)
                profileDb = ProflieDB?.getInstace(mContext)
            }
        }
    })

}
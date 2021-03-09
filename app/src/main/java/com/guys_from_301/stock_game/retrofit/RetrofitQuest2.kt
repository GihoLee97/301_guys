package com.guys_from_301.stock_game

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.security.MessageDigest
import kotlin.experimental.and

interface RetrofitQuest2 {
    @FormUrlEncoded
    @POST("/test/quest2.php/")
    fun quest2(
            @Field("u_id") u_id: String,
            @Field("u_quest2") u_quest2: Int
    ): Call<String>
}

// update
fun quest2(u_id: String, u_quest2:Int) {
    var funquest2: RetrofitQuest2? = null
    val url = "http://stockgame.dothome.co.kr/test/quest2.php/"
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
    funquest2= retrofit.create(RetrofitQuest2::class.java)
    funquest2.quest2(u_id, u_quest2).enqueue(object : Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable) {
            println("---서버업데이트실패: "+t.message)
        }
        override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
            if (response.isSuccessful && response.body() != null) {
                println("---서버업데이트성공")
            }
        }
    })

}

package com.guys_from_301.stock_game.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.RetrofitUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitQuest {
    @FormUrlEncoded
    @POST("/test/quest.php/")
    fun quest(
            @Field("u_id") u_id: String,
            @Field("u_pw") u_pw: String,
            @Field("u_quest") u_quest: Int)
            : Call<String>
}

// quest
fun postquest(u_id: String, u_pw: String, u_quest:Int) {
    var funquest: RetrofitQuest? = null
    val url = "http://stockgame.dothome.co.kr/test/quest.php/"
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
    funquest= retrofit.create(RetrofitQuest::class.java)
    funquest.quest(u_id, u_pw, u_quest).enqueue(object : Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable) {
            println("---업적업데이트실패: "+t.message)
        }
        override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
            if (response.isSuccessful && response.body() != null) {
                println("---업적업데이트성공")
            }
        }
    })

}
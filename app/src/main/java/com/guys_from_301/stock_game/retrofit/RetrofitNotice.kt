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

// NOTICE
fun getnotice(u_id: String) {
    var funnotice: RetrofitNotice? = null
    val url = "http://stockgame.dothome.co.kr/test/notice_get.php/"
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
    funnotice= retrofit.create(RetrofitNotice::class.java)
    funnotice.getnotice(u_id).enqueue(object : Callback<MutableList<DATACLASS_NOTICE>> {
        override fun onFailure(call: Call<MutableList<DATACLASS_NOTICE>>, t: Throwable) {
            println("---서버통신실패: "+t.message)
        }
        override fun onResponse(call: Call<MutableList<DATACLASS_NOTICE>>, response: retrofit2.Response<MutableList<DATACLASS_NOTICE>>) {
            if (response.isSuccessful && response.body() != null) {
                println("---서버통신성공")
                println("---"+ response.body()!![0].TITLE)
                println("---"+ response.body()!![0].CONTENT)
                println("---"+ response.body()!![1].TITLE)
                println("---"+ response.body()!![1].CONTENT)
            }
        }
    })

}



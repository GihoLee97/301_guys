package com.guys_from_301.stock_game.retrofit

import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.DATACLASS
import com.guys_from_301.stock_game.RetrofitUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitImage {
    @FormUrlEncoded
    @POST("/test/setimage.php/")
    fun setimage(
            @Field("u_id") u_id : String,
            @Field("u_imagenumber") u_imagenumber : Int
    ): Call<String>
}

// setimage
fun setimage(u_id: String, u_imagenumber: Int) {
    var funsetimage: RetrofitImage? = null
    val url = "http://stockgame.dothome.co.kr/test/setimage.php/"
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
    funsetimage= retrofit.create(RetrofitImage::class.java)
    funsetimage.setimage(u_id, u_imagenumber).enqueue(object : Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable) {
            println("---서버업데이트실패: "+t.message)
        }
        override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
            if (response.isSuccessful && response.body() != null) {
                if(response.body()!! == "555"){
                    println("---서버업데이트성공")
                }
                else if(response.body()!! == "444"){
                    println("---아이디 일치하지 않음")
                }
                else{
                    println("---나중에 다시 시도")
                }
            }
        }
    })

}


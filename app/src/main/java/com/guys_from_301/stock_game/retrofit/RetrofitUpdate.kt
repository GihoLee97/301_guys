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

interface RetrofitUpdate {
    @FormUrlEncoded
    @POST("/test/update.php/")
    fun update(
        @Field("u_id") u_id: String,
        @Field("u_pw") u_pw: String,
        @Field("u_money") u_money: Int,
        @Field("u_value1") u_value1: Int,
        @Field("u_nickname") u_nickname: String,
        @Field("u_profit") u_profit: Float,
        @Field("u_roundcount") u_roundcount: Int,
        @Field("u_history") u_history: Int,
        @Field("u_level") u_level: Int)
            : Call<String>
}

// update
fun update(u_id: String, u_pw: String,u_money : Int, u_value1: Int, u_nickname:String, u_profit:Float, u_roundcount:Int, u_history:Int, u_level:Int) {
    var funupdate: RetrofitUpdate? = null
    val url = "http://stockgame.dothome.co.kr/test/update.php/"
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
    funupdate= retrofit.create(RetrofitUpdate::class.java)
    funupdate.update(u_id, u_pw, u_money, u_value1, u_nickname, u_profit, u_roundcount, u_history, u_level).enqueue(object : Callback<String> {
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
fun getHash(input : String):String{
    val salt1 = "We-are-301-guys"
    val salt2 = "We-are-gonna-be-rich"
    var adjusted_input_1 = input+salt1
    var adjusted_input_2 = input+salt2
    var messagedigest = MessageDigest.getInstance("SHA-256")
    var result1 = String(messagedigest.digest(adjusted_input_1.hashCode().toString().toByteArray()))
    var result2 = String(messagedigest.digest(adjusted_input_2.hashCode().toString().toByteArray()))
    var adjusted_input_3 = result1+result2
    var result3 = messagedigest.digest(adjusted_input_3.hashCode().toString().toByteArray())

    var sb: StringBuilder = StringBuilder()

    var i = 0
    while (i < result3.count()) {
        sb.append(((result3[i].and(0xff.toByte())) + 0x100).toString(16).substring(0, 1))
        i++
    }

    var final_result = sb.toString()
    return final_result
}


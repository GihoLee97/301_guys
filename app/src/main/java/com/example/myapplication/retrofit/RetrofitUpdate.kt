package com.example.myapplication

import android.widget.Toast
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
        @Field("u_nickname") u_nickname: String,
        @Field("u_profit") u_profit: Int,
        @Field("u_history") u_history: String,
        @Field("u_level") u_level: Int)
            : Call<String>
}

// update
fun update(u_id: String, u_pw: String,u_money : Int, u_nickname:String, u_profit:Int, u_history:String, u_level:Int) {
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
    funupdate.update(u_id, u_pw, u_money, u_nickname, u_profit, u_history, u_level).enqueue(object : Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable) {
            //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
        }
        override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
            if (response.isSuccessful && response.body() != null) {
//                val okcode: String = response.body()!!
//                if (okcode == "7774"){
                //Toast.makeText(this@InitialActivity, "로그인 성공!", Toast.LENGTH_LONG).show()
                //loginSuccess("GENERAL") // memorize login method and move to MainActivity
            }
//                if (okcode == "4"){
            //Toast.makeText(this@InitialActivity, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
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


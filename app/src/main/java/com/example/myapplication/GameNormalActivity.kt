package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameNormalActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_normal)
        val buy_btn = findViewById<Button>(R.id.buy_btn)
        buy_btn.setOnClickListener {
            val dlg_buy = Dialog_buy(this)
            val layoutInflater_buy: LayoutInflater = getLayoutInflater()
            val builder_buy = AlertDialog.Builder(this)
            dlg_buy.start()
        }

        val sell_btn = findViewById<Button>(R.id.sell_btn)
        sell_btn.setOnClickListener {
            val dlg_sell = Dialog_sell(this)
            val layoutInflater_sell: LayoutInflater = getLayoutInflater()
            val builder_sell = AlertDialog.Builder(this)
            dlg_sell.start()
        }
        val auto_btn = findViewById<Button>(R.id.auto_btn)
        auto_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
//            val u_id = " "
//            val u_pw = " "
//            val u_date = " "
//            getRoomListDataHttp(u_id, u_pw, u_date)
            getRoomListDataHttp()
        }

        val item_btn = findViewById<Button>(R.id.item_btn)
        item_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.item_pick_dialog,null)
            builder.setView(dialogview).show()
        }
    }
    // 데이터 가지고 오기
    fun getRoomListDataHttp(){
        val u_id = ""
        val u_pw = ""
        val u_date = ""
        val url: String = "http://stockgame.dothome.co.kr/test/call.php/"
        Log.d("데이터 받기 ","받기시도 중")
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
        var server = retrofit.create(RetrofitGet::class.java)
        server.getdata(u_id, u_pw, u_date).enqueue(object : Callback<String> {
        //server.getdata().enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                //Toast.makeText(this@Initial, " ", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                //Toast.makeText(this@Initial, "bbbbbbb", Toast.LENGTH_LONG).show()
                if (response.isSuccessful && response.body() != null) {
                    val getted_name: String = response.body()!!
                    Toast.makeText(this@GameNormalActivity, getted_name, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}
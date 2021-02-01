package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
        //변수 선언
        val buy_btn = findViewById<Button>(R.id.buy_btn)
        val startcash: Float = 5000000F
        val startevaluation: Float = 0F
        val startprofit: Float = 0F
        val cash = findViewById<TextView>(R.id.cash)
        val evaluation = findViewById<TextView>(R.id.evaluation)
        val profit = findViewById<TextView>(R.id.profit)
        //Buy Dialog로 부터 결과를 받아오는 list
        lateinit var buy: List<Float>
        lateinit var sell: List<Float>
        //viewModel 객체
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GameNormalActivityVeiwModel::class.java)

        //초기화
        viewModel.initialize(startcash, startevaluation, startprofit)
        viewModel.cash().observe(this, Observer {
            cash.text = "현금: "+it.toString()+"원"
        })
        viewModel.evaluation().observe(this, Observer {
            evaluation.text = "원화평가금액: "+it.toString()+"원"
        })
        viewModel.profit().observe(this, Observer {
            profit.text = "수익률"+it.toString()+"%"
        })



        //Button 동작

        //매수
       buy_btn.setOnClickListener {
            val dlg_buy = Dialog_buy(this)
            val layoutInflater_buy: LayoutInflater = getLayoutInflater()
            val builder_buy = AlertDialog.Builder(this)
            dlg_buy.start()
            dlg_buy.setOnBuyClickedListener { content->
                buy=content
                viewModel.buyStock(buy[0], buy[1])
            }
        }

        //매도
        val sell_btn = findViewById<Button>(R.id.sell_btn)
        sell_btn.setOnClickListener {
            val dlg_sell = Dialog_sell(this)
            val layoutInflater_sell: LayoutInflater = getLayoutInflater()
            val builder_sell = AlertDialog.Builder(this)
            dlg_sell.start()
            dlg_sell.setOnSellClickedListener { content->
                sell=content
                viewModel.sellStock(sell[0], sell[1])
            }
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
            override fun onFailure(call: Call<String>, t: Throwable) {
                //Toast.makeText(this@Initial, " ", Toast.LENGTH_LONG).show()
                //Log.d("data: ",data)
            }
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                //Toast.makeText(this@Initial, "bbbbbbb", Toast.LENGTH_LONG).show()
                if (response.isSuccessful && response.body() != null) {
                    val getted_name: String = response.body()!!
                    Toast.makeText(this@GameNormalActivity, getted_name, Toast.LENGTH_LONG).show()
                    Log.d("---:",response.isSuccessful.toString())
                   // Toast.makeText(this@GameNormalActivity, response.isSuccessful, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}